package com.hcl.faultalertengine.service;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.rule.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RuleEngineService {

    private final AlertService alertService;
    private final List<Rule> rules;

    public RuleEngineService(AlertService alertService, List<Rule> rules){
        this.alertService = alertService;
        this.rules = rules;
    }
    
    public Optional<List<AlertMetadata>> evaluateRules(DeviceEvent event){
        log.info("Received event for device: {}", event.getDeviceId());
        List<AlertMetadata> alerts = new ArrayList<>();
        for(Rule rule : rules){
            rule.evaluate(event).ifPresent(alerts::add);
        }
        Set<String> triggeredAlertTypes = alerts.stream()
                .map(AlertMetadata::getAlertType)
                .collect(Collectors.toSet());

        if(alerts.isEmpty()){
            log.info("No alerts generated for device {}", event.getDeviceId());
            alertService.resolveRecoveredAlerts(event, triggeredAlertTypes);
            return Optional.empty();
        }

        alerts.forEach(alert -> {
            log.info("Rule triggered: {} for device {}", alert.getAlertType(), event.getDeviceId());
            alertService.processAlert(event, alert);
        });

        alertService.resolveRecoveredAlerts(event, triggeredAlertTypes);
        return Optional.of(alerts);
    }
}
