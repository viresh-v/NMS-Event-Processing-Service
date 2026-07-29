package com.hcl.faultalertengine.service.impl;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.AlertResponse;
import com.hcl.faultalertengine.dto.AlertSearchRequest;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.enums.AlertAction;
import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import com.hcl.faultalertengine.exception.AlertNotFoundException;
import com.hcl.faultalertengine.exception.InvalidAlertStateException;
import com.hcl.faultalertengine.factory.AlertFactory;
import com.hcl.faultalertengine.mapper.AlertMapper;
import com.hcl.faultalertengine.repository.AlertRepository;
import com.hcl.faultalertengine.service.AlertHistoryService;
import com.hcl.faultalertengine.service.AlertService;
import com.hcl.faultalertengine.specification.AlertSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository repository;
    private final AlertFactory alertFactory;
    private final AlertMapper alertMapper;
    private final AlertHistoryService alertHistoryService;

    @Override
    public void processAlert(DeviceEvent event, AlertMetadata metadata){
        Optional<Alert> existingAlert = repository.findByDeviceIdAndAlertTypeAndStatus(
                event.getDeviceId(),metadata.getAlertType(), AlertStatus.OPEN
        );
        if(existingAlert.isPresent()){
            updateExistingAlert(existingAlert.get(), metadata);
            return;
        }
        createNewAlert(event, metadata);
    }

    private void updateExistingAlert(Alert alert, AlertMetadata metadata){
        AlertStatus oldStatus = alert.getStatus();
        Severity oldSeverity = alert.getSeverity();
        log.warn("Escalating alert {} from {} to {}", alert.getId(), oldSeverity, metadata.getSeverity());
        alert.setOccurrenceCount(alert.getOccurrenceCount() + 1);
        alert.setUpdatedTime(LocalDateTime.now());
        alert.setSeverity(metadata.getSeverity());
        Alert updatedAlert = repository.save(alert);

        if(!updatedAlert.getSeverity().equals(oldSeverity)){
            AlertAction action = updatedAlert.getSeverity().ordinal() > oldSeverity.ordinal()
                    ? AlertAction.ESCALATED : AlertAction.DE_ESCALATED;

            alertHistoryService.recordHistory(
                    updatedAlert,
                    oldStatus,
                    updatedAlert.getStatus(),
                    action,
                    "Severity escalated from "+oldSeverity+" to "+updatedAlert.getSeverity()+"."
            );
        }
        log.info("Alert {} updated successfully.", updatedAlert.getId());
    }

    private void createNewAlert(DeviceEvent event, AlertMetadata metadata){
        Alert alert = alertFactory.createAlert(event, metadata);
        log.info("Creating new alert for device: {}, alertType: {}, severity: {}", alert.getDeviceId(), alert.getAlertType(), alert.getSeverity());
        Alert savedAlert = repository.save(alert);

        alertHistoryService.recordHistory(
                savedAlert,
                null,
                AlertStatus.OPEN,
                AlertAction.CREATED,
                "New alert generated."
        );
        log.info("Alert created successfully. Alert Id: {}", savedAlert.getId());
    }

    @Override
    public List<AlertResponse> getAllAlerts(){
        return repository.findAll()
                .stream()
                .map(alertMapper::toResponse)
                .toList();
    }

    @Override
    public AlertResponse getAlertById(Long id){
        Alert alert = getAlertEntity(id);

        return alertMapper.toResponse(alert);
    }

    @Override
    public List<AlertResponse> getOpenAlerts(){
        return repository.findByStatus(AlertStatus.OPEN)
                .stream()
                .map(alertMapper::toResponse)
                .toList();
    }

    @Override
    public AlertResponse acknowledgeAlert(Long id){
        log.info("Acknowledging alert {}", id);
        Alert alert = getAlertEntity(id);
        AlertStatus oldStatus = alert.getStatus();
        if (alert.getStatus() == AlertStatus.RESOLVED){
            throw new InvalidAlertStateException("Resolved alert cannot be acknowledged.");
        }
        if (alert.getStatus() == AlertStatus.ACKNOWLEDGED){
            throw new InvalidAlertStateException("Alert is already acknowledged.");
        }
        alert.setStatus(AlertStatus.ACKNOWLEDGED);
        alert.setUpdatedTime(LocalDateTime.now());

        Alert updatedAlert = repository.save(alert);
        alertHistoryService.recordHistory(
                updatedAlert,
                oldStatus,
                AlertStatus.ACKNOWLEDGED,
                AlertAction.ACKNOWLEDGED,
                "Alert acknowledged."
        );
        log.info("Alert {} acknowledged successfully.", alert.getId());
        return alertMapper.toResponse(alert);
    }

    @Override
    public AlertResponse resolveAlert(Long id){
        log.info("Resolving alert {} manually", id);
        Alert alert = getAlertEntity(id);
        AlertStatus oldStatus = alert.getStatus();
        if (alert.getStatus() == AlertStatus.RESOLVED){
            throw new InvalidAlertStateException("Alert is already resolved.");
        }
        alert.setStatus(AlertStatus.RESOLVED);
        alert.setUpdatedTime(LocalDateTime.now());
        alert.setResolvedTime(LocalDateTime.now());

        Alert updatedAlert = repository.save(alert);
        alertHistoryService.recordHistory(
                updatedAlert,
                oldStatus,
                AlertStatus.RESOLVED,
                AlertAction.MANUAL_RESOLVED,
                "Alert resolved manually."
        );
        log.info("Alert {} resolved successfully.", alert.getId());
        return alertMapper.toResponse(alert);
    }

    private Alert getAlertEntity(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id));
    }

    @Override
    public void resolveRecoveredAlerts(DeviceEvent event, Set<String> triggeredAlertTypes){
        List<Alert> alerts = repository.findByDeviceIdAndStatus(
                event.getDeviceId(),
                AlertStatus.OPEN
        );
        for(Alert alert : alerts){
            if(triggeredAlertTypes.contains(alert.getAlertType())){
                continue;
            }
            log.info("Alert {} recovered for device {}", alert.getAlertType(), alert.getDeviceId());
            alert.setStatus(AlertStatus.RESOLVED);
            alert.setMessage("Device metrics returned to normal.");
            alert.setResolvedTime(LocalDateTime.now());
            repository.save(alert);

            alertHistoryService.recordHistory(
                    alert,
                    AlertStatus.OPEN,
                    AlertStatus.RESOLVED,
                    AlertAction.AUTO_RESOLVED,
                    "Device recovered automatically."
            );
        }
    }

    @Override
    public Page<AlertResponse> searchAlerts(AlertSearchRequest request, Pageable pageable){
        return repository.findAll(AlertSpecification.search(request),pageable)
                .map(alertMapper::toResponse);
    }
}
