package com.hcl.faultalertengine.factory;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.enums.AlertStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AlertFactory {

    public Alert createAlert(DeviceEvent event, AlertMetadata metadata){
        Alert alert = new Alert();
        alert.setDeviceId(event.getDeviceId());
        alert.setIpAddress(event.getIpAddress());
        alert.setAlertType(metadata.getAlertType());
        alert.setSeverity(metadata.getSeverity());
        alert.setStatus(AlertStatus.OPEN);
        alert.setMessage(metadata.getMessage());
        alert.setOccurrenceCount(1);
        alert.setCreatedTime(LocalDateTime.now());
        alert.setUpdatedTime(LocalDateTime.now());

        return alert;
    }
}
