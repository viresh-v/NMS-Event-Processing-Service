package com.hcl.faultalertengine.service;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.enums.AlertAction;
import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import com.hcl.faultalertengine.exception.InvalidAlertStateException;
import com.hcl.faultalertengine.factory.AlertFactory;
import com.hcl.faultalertengine.mapper.AlertMapper;
import com.hcl.faultalertengine.repository.AlertRepository;
import com.hcl.faultalertengine.service.impl.AlertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertMapper alertMapper;

    @Mock
    private AlertFactory alertFactory;

    @Mock
    private AlertHistoryService alertHistoryService;

    @InjectMocks
    private AlertServiceImpl alertService;

    private DeviceEvent event;
    private AlertMetadata metadata;
    private Alert alert;

    @BeforeEach
    void setUp() {

        event = DeviceEvent.builder()
                .deviceId("DEVICE-001")
                .cpuUsage(95.0)
                .memoryUsage(60.0)
                .activeConnections(100.0)
                .build();

        metadata = AlertMetadata.builder()
                .alertType("CPU")
                .severity(Severity.CRITICAL)
                .message("CPU threshold exceeded")
                .build();

        alert = Alert.builder()
                .id(1L)
                .deviceId("DEVICE-001")
                .alertType("CPU")
                .severity(Severity.CRITICAL)
                .status(AlertStatus.OPEN)
                .occurrenceCount(1)
                .build();
    }

    @Test
    void shouldCreateNewAlert() {

        when(alertRepository.findByDeviceIdAndAlertTypeAndStatus(
                anyString(),
                anyString(),
                eq(AlertStatus.OPEN)))
                .thenReturn(Optional.empty());

        when(alertFactory.createAlert(event, metadata))
                .thenReturn(alert);

        when(alertRepository.save(alert))
                .thenReturn(alert);

        alertService.processAlert(event, metadata);

        verify(alertRepository).save(alert);

        verify(alertHistoryService)
                .recordHistory(
                        eq(alert),
                        isNull(),
                        eq(AlertStatus.OPEN),
                        eq(AlertAction.CREATED),
                        anyString());
    }

    @Test
    void shouldEscalateExistingAlert() {

        Alert existing = Alert.builder()
                .id(1L)
                .deviceId("DEVICE-001")
                .alertType("CPU")
                .severity(Severity.WARN)
                .status(AlertStatus.OPEN)
                .occurrenceCount(1)
                .build();

        when(alertRepository.findByDeviceIdAndAlertTypeAndStatus(
                anyString(),
                anyString(),
                eq(AlertStatus.OPEN)))
                .thenReturn(Optional.of(existing));

        when(alertRepository.save(any(Alert.class)))
                .thenReturn(existing);

        alertService.processAlert(event, metadata);

        verify(alertRepository).save(existing);

        verify(alertHistoryService)
                .recordHistory(
                        any(Alert.class),
                        any(),
                        any(),
                        eq(AlertAction.ESCALATED),
                        anyString());
    }

    @Test
    void shouldIncrementOccurrenceForDuplicateAlert() {

        Alert existing = Alert.builder()
                .id(1L)
                .deviceId("DEVICE-001")
                .alertType("CPU")
                .severity(Severity.CRITICAL)
                .status(AlertStatus.OPEN)
                .occurrenceCount(1)
                .build();

        when(alertRepository.findByDeviceIdAndAlertTypeAndStatus(
                anyString(),
                anyString(),
                eq(AlertStatus.OPEN)))
                .thenReturn(Optional.of(existing));

        when(alertRepository.save(any(Alert.class)))
                .thenReturn(existing);

        alertService.processAlert(event, metadata);

        assertEquals(2, existing.getOccurrenceCount());

        verify(alertRepository).save(existing);
    }

    @Test
    void shouldAcknowledgeAlert() {

        when(alertRepository.findById(1L))
                .thenReturn(Optional.of(alert));

        when(alertRepository.save(any(Alert.class)))
                .thenReturn(alert);

        alertService.acknowledgeAlert(1L);

        assertEquals(AlertStatus.ACKNOWLEDGED,
                alert.getStatus());

        verify(alertHistoryService)
                .recordHistory(
                        any(),
                        any(),
                        eq(AlertStatus.ACKNOWLEDGED),
                        eq(AlertAction.ACKNOWLEDGED),
                        anyString());
    }

    @Test
    void shouldResolveAlert() {

        when(alertRepository.findById(1L))
                .thenReturn(Optional.of(alert));

        when(alertRepository.save(any(Alert.class)))
                .thenReturn(alert);

        alertService.resolveAlert(1L);

        assertEquals(AlertStatus.RESOLVED,
                alert.getStatus());

        verify(alertHistoryService)
                .recordHistory(
                        any(),
                        any(),
                        eq(AlertStatus.RESOLVED),
                        eq(AlertAction.MANUAL_RESOLVED),
                        anyString());
    }

    @Test
    void shouldThrowExceptionWhenAcknowledgingResolvedAlert() {

        alert.setStatus(AlertStatus.RESOLVED);

        when(alertRepository.findById(1L))
                .thenReturn(Optional.of(alert));

        assertThrows(
                InvalidAlertStateException.class,
                () -> alertService.acknowledgeAlert(1L));

        verify(alertRepository, never())
                .save(any());
    }
}