package com.hcl.faultalertengine.consumer;

import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.service.RuleEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeviceEventConsumer {

    private final RuleEngineService ruleEngineService;

    public DeviceEventConsumer(RuleEngineService ruleEngineService){
        this.ruleEngineService = ruleEngineService;
    }

    @KafkaListener(
            topics = "${kafka.topic.validated-events}",
            groupId = "fault-alert-group"
    )
    public void consume(DeviceEvent event){
        log.info("Received Event : {}", event);
        ruleEngineService.evaluateRules(event);
    }
}
