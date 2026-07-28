package com.nms.eventprocessing.consumer;

import com.nms.eventprocessing.service.EventProcessingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TowerEventConsumer {

    private final EventProcessingService eventProcessingService;

    public TowerEventConsumer(EventProcessingService eventProcessingService) {
        this.eventProcessingService = eventProcessingService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.network-events-raw}",
            groupId = "${spring.kafka.consumer.group-id}"
    )


    public void consumeTowerEvent(String rawEventPayload) {
        eventProcessingService.processDeviceEvent(rawEventPayload);
    }
}
