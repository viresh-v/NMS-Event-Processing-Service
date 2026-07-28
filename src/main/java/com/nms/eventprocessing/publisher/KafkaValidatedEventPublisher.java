package com.nms.eventprocessing.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nms.eventprocessing.entity.DeviceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaValidatedEventPublisher implements ValidatedEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaValidatedEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String validatedEventsTopic;

    public KafkaValidatedEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${app.kafka.topics.validated-events}") String validatedEventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.validatedEventsTopic = validatedEventsTopic;
    }

    @Override
    public void publish(DeviceEvent deviceEvent) {
        try {
            String payload = objectMapper.writeValueAsString(deviceEvent);
            kafkaTemplate.send(validatedEventsTopic, deviceEvent.getDeviceId(), payload)
                    .whenComplete((result, exception) -> {
                        if (exception == null) {
                            LOGGER.info("Published validated event. eventId={}, topic={}",
                                    deviceEvent.getId(), validatedEventsTopic);
                        } else {
                            LOGGER.error("Failed to publish validated event. eventId={}",
                                    deviceEvent.getId(), exception);
                        }
                    });
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize validated device event", exception);
        }
    }
}
