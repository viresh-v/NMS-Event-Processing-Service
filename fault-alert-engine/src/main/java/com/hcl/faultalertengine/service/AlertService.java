package com.hcl.faultalertengine.service;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.AlertResponse;
import com.hcl.faultalertengine.dto.AlertSearchRequest;
import com.hcl.faultalertengine.dto.DeviceEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface AlertService {

    void processAlert(DeviceEvent event, AlertMetadata metadata);

    List<AlertResponse> getAllAlerts();

    AlertResponse getAlertById(Long id);

    List<AlertResponse> getOpenAlerts();

    AlertResponse acknowledgeAlert(Long id);

    AlertResponse resolveAlert(Long id);

    void resolveRecoveredAlerts(DeviceEvent event, Set<String> triggeredAlertTypes);

    Page<AlertResponse> searchAlerts(AlertSearchRequest request, Pageable pageable);
}
