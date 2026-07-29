package com.hcl.faultalertengine.factory;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    private AlertFactory alertFactory;

    @BeforeEach
    void setUp() {
        alertFactory = new AlertFactory();
    }

    @Test
    void shouldCreateAlertSuccessfully() {

        DeviceEvent event = DeviceEvent.builder()
                .deviceId("DEVICE-001")
                .cpuUsage(95.0)
                .memoryUsage(70.0)
                .activeConnections(5000.0)
                .build();

        AlertMetadata metadata = new AlertMetadata(
                "CPU",
                Severity.CRITICAL,
                "CPU threshold exceeded"
        );

        Alert alert = alertFactory.createAlert(event, metadata);

        assertNotNull(alert);

        assertEquals("DEVICE-001", alert.getDeviceId());
        assertEquals("CPU", alert.getAlertType());
        assertEquals(Severity.CRITICAL, alert.getSeverity());
        assertEquals(AlertStatus.OPEN, alert.getStatus());
        assertEquals("CPU threshold exceeded", alert.getMessage());

        assertEquals(1, alert.getOccurrenceCount());

        assertNotNull(alert.getCreatedTime());
        assertNotNull(alert.getUpdatedTime());
    }

}