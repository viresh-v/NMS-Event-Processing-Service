package com.nms.eventprocessing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nms.eventprocessing.entity.DeviceEvent;
import com.nms.eventprocessing.publisher.ValidatedEventPublisher;
import com.nms.eventprocessing.repository.DeviceEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessingService.class);

    private final ObjectMapper objectMapper;
    private final DeviceEventRepository deviceEventRepository;
    private final ValidatedEventPublisher validatedEventPublisher;

    public EventProcessingService(
            ObjectMapper objectMapper,
            DeviceEventRepository deviceEventRepository,
            ValidatedEventPublisher validatedEventPublisher
    ) {
        this.objectMapper = objectMapper;
        this.deviceEventRepository = deviceEventRepository;
        this.validatedEventPublisher = validatedEventPublisher;
    }


    private record DeviceMetricPayload(
            String deviceId,
            String ipAddress,
            Double cpuUsage,
            Double memoryUsage,
            Integer activeConnections,
            String timestamp
    ) {}

    private boolean isValid(DeviceMetricPayload payload) {
        return payload != null
                && hasText(payload.deviceId())
                && hasText(payload.ipAddress())
                && isPercentage(payload.cpuUsage())
                && isPercentage(payload.memoryUsage())
                && payload.activeConnections() != null
                && hasText(payload.timestamp());
    }


    public void processDeviceEvent(String rawEventPayload) {
        try {
            DeviceMetricPayload payload = objectMapper.readValue(rawEventPayload, DeviceMetricPayload.class);

            if (!isValid(payload)) {
                LOGGER.warn("Skipping invalid tower event payload: {}", rawEventPayload);
                return;
            }

            DeviceEvent deviceEvent = new DeviceEvent(
                    payload.deviceId(),
                    payload.ipAddress(),
                    payload.cpuUsage(),
                    payload.memoryUsage(),
                    payload.activeConnections(),
                    payload.timestamp()
            );

            DeviceEvent savedEvent = deviceEventRepository.save(deviceEvent);
            validatedEventPublisher.publish(savedEvent);
        } catch (JsonProcessingException exception) {
            LOGGER.warn("Skipping unreadable tower event payload: {}", rawEventPayload, exception);
        }
    }

    public List<DeviceEvent> getAllProcessedEvents() {
        return deviceEventRepository.findAll();
    }

    public Optional<DeviceEvent> getProcessedEventById(Long id) {
        return deviceEventRepository.findById(id);
    }



    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isPercentage(Double value) {
        return value != null && value >= 0.0 && value <= 100.0;
    }

    private boolean isReasonableTemperature(Double value) {
        return value != null && value >= -50.0 && value <= 150.0;
    }


}
