package com.hcl.faultalertengine.mapper;

import com.hcl.faultalertengine.dto.AlertResponse;
import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlertMapperTest {

    private AlertMapper alertMapper;

    @BeforeEach
    void setUp() {
        alertMapper = new AlertMapper();
    }

    @Test
    void shouldMapAlertToAlertResponse() {

        Alert alert = Alert.builder()
                .id(1L)
                .deviceId("DEVICE-001")
                .alertType("CPU")
                .severity(Severity.CRITICAL)
                .status(AlertStatus.OPEN)
                .message("CPU threshold exceeded")
                .occurrenceCount(3)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        AlertResponse response = alertMapper.toResponse(alert);

        assertNotNull(response);

        assertEquals(alert.getId(), response.getId());
        assertEquals(alert.getDeviceId(), response.getDeviceId());
        assertEquals(alert.getAlertType(), response.getAlertType());
        assertEquals(alert.getSeverity(), response.getSeverity());
        assertEquals(alert.getStatus(), response.getStatus());
        assertEquals(alert.getMessage(), response.getMessage());
        assertEquals(alert.getOccurrenceCount(), response.getOccurenceCount());
    }

}