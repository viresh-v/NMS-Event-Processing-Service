package com.hcl.faultalertengine.mapper;

import com.hcl.faultalertengine.dto.AlertResponse;
import com.hcl.faultalertengine.entity.Alert;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public AlertResponse toResponse(Alert alert){
        return AlertResponse.builder()
                .id(alert.getId())
                .deviceId(alert.getDeviceId())
                .ipAddress(alert.getIpAddress())
                .alertType(alert.getAlertType())
                .severity(alert.getSeverity())
                .message(alert.getMessage())
                .status(alert.getStatus())
                .occurenceCount(alert.getOccurrenceCount())
                .createdTime(alert.getCreatedTime())
                .updatedTime(alert.getUpdatedTime())
                .resolvedTime(alert.getResolvedTime())
                .build();
    }
}
