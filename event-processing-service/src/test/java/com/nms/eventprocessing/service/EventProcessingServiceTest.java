package com.nms.eventprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nms.eventprocessing.entity.DeviceEvent;
import com.nms.eventprocessing.publisher.ValidatedEventPublisher;
import com.nms.eventprocessing.repository.DeviceEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventProcessingServiceTest {

    @Mock
    private DeviceEventRepository deviceEventRepository;

    @Mock
    private ValidatedEventPublisher validatedEventPublisher;

    private EventProcessingService eventProcessingService;

    @BeforeEach
    void setUp() {
        eventProcessingService = new EventProcessingService(
                new ObjectMapper(),
                deviceEventRepository,
                validatedEventPublisher
        );
    }

    @Test
    void processDeviceEvent_shouldSaveAndForwardEvent_whenPayloadIsValid() {

        String rawEventPayload = """
                {
                  "deviceId": "DEVICE-001",
                  "ipAddress": "192.168.1.10",
                  "cpuUsage": 72.5,
                  "memoryUsage": 60.25,
                  "activeConnections": 356,
                  "timestamp": "2026-07-10 15:30:12"
                }
                """;

        DeviceEvent savedEvent = new DeviceEvent(
                "DEVICE-001",
                "192.168.1.10",
                72.5,
                60.25,
                356,
                "2026-07-10 15:30:12"
        );

        when(deviceEventRepository.save(any(DeviceEvent.class))).thenReturn(savedEvent);

        eventProcessingService.processDeviceEvent(rawEventPayload);

        ArgumentCaptor<DeviceEvent> captor = ArgumentCaptor.forClass(DeviceEvent.class);

        verify(deviceEventRepository).save(captor.capture());
        verify(validatedEventPublisher).publish(savedEvent);

        DeviceEvent captured = captor.getValue();

        assertEquals("DEVICE-001", captured.getDeviceId());
        assertEquals("192.168.1.10", captured.getIpAddress());
        assertEquals(72.5, captured.getCpuUsage());
        assertEquals(60.25, captured.getMemoryUsage());
        assertEquals(356, captured.getActiveConnections());
        assertEquals("2026-07-10 15:30:12", captured.getTimestamp());
    }

    @Test
    void processDeviceEvent_shouldSkipEvent_whenPayloadHasInvalidCpuUsage() {

        String rawEventPayload = """
                {
                  "deviceId": "DEVICE-001",
                  "ipAddress": "192.168.1.10",
                  "cpuUsage": 120.0,
                  "memoryUsage": 60.25,
                  "activeConnections": 356,
                  "timestamp": "2026-07-10 15:30:12"
                }
                """;

        eventProcessingService.processDeviceEvent(rawEventPayload);

        verify(deviceEventRepository, never()).save(any(DeviceEvent.class));
        verify(validatedEventPublisher, never()).publish(any(DeviceEvent.class));
    }

    @Test
    void processDeviceEvent_shouldSkipEvent_whenPayloadIsNotReadableJson() {

        String rawEventPayload = "not-a-json-payload";

        eventProcessingService.processDeviceEvent(rawEventPayload);

        verify(deviceEventRepository, never()).save(any(DeviceEvent.class));
        verify(validatedEventPublisher, never()).publish(any(DeviceEvent.class));
    }
}
