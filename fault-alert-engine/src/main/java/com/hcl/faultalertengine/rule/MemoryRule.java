package com.hcl.faultalertengine.rule;

import com.hcl.faultalertengine.config.RuleProperties;
import com.hcl.faultalertengine.constants.AlertMessage;
import com.hcl.faultalertengine.constants.AlertType;
import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.enums.Severity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemoryRule implements Rule{

    private final RuleProperties ruleProperties;

    public MemoryRule(RuleProperties ruleProperties) {
        this.ruleProperties = ruleProperties;
    }

    @Override
    public Optional<AlertMetadata> evaluate(DeviceEvent event){
        Severity severity = null;
        String message = null;
        if(event.getMemoryUsage() >= ruleProperties.getMemory().getCritical()){
            severity = Severity.CRITICAL;
            message = AlertMessage.MEMORY_THRESHOLD_EXCEEDED + ruleProperties.getMemory().getCritical() + AlertMessage.PERCENTAGE;
        }
        else if(event.getMemoryUsage() >= ruleProperties.getMemory().getMajor()){
            severity = Severity.MAJOR;
            message = AlertMessage.MEMORY_THRESHOLD_EXCEEDED + ruleProperties.getMemory().getMajor() + AlertMessage.PERCENTAGE;
        }
        else if(event.getMemoryUsage() >= ruleProperties.getMemory().getWarning()){
            severity = Severity.WARN;
            message = AlertMessage.MEMORY_THRESHOLD_EXCEEDED + ruleProperties.getMemory().getWarning() + AlertMessage.PERCENTAGE;
        }
        else {
            return Optional.empty();
        }
        return Optional.of(
            new AlertMetadata(
                AlertType.MEMORY,
                severity,
                message
            )
        );
    }
}
