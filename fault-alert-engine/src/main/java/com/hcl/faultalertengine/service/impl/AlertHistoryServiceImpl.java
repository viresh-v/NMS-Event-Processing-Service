package com.hcl.faultalertengine.service.impl;

import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.entity.AlertHistory;
import com.hcl.faultalertengine.enums.AlertAction;
import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.repository.AlertHistoryRepository;
import com.hcl.faultalertengine.service.AlertHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertHistoryServiceImpl implements AlertHistoryService {

    private final AlertHistoryRepository repository;

    @Override
    public void recordHistory(Alert alert, AlertStatus oldStatus, AlertStatus newStatus, AlertAction action, String remarks){
        log.info("Recording history. Alert Id: {}, Action: {}", alert.getId(), action);
        AlertHistory history = AlertHistory.builder()
                .alertId(alert.getId())
                .deviceId(alert.getDeviceId())
                .alertType(alert.getAlertType())
                .severity(alert.getSeverity())
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .action(action)
                .remarks(remarks)
                .changedTime(LocalDateTime.now())
                .build();

        repository.save(history);
        log.debug("History recorded successfully for Alert {}", alert.getId());
    }
}
