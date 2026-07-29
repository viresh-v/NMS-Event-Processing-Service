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
public class CpuRule implements Rule {

    private final RuleProperties ruleProperties;

    public CpuRule(RuleProperties ruleProperties) {
        this.ruleProperties = ruleProperties;
    }

    @Override
    public Optional<AlertMetadata> evaluate(DeviceEvent event){
        Severity severity = null;
        String message = null;
        if(event.getCpuUsage() >= ruleProperties.getCpu().getCritical()){
            severity = Severity.CRITICAL;
            message = AlertMessage.CPU_THRESHOLD_EXCEEDED + ruleProperties.getCpu().getCritical() + AlertMessage.PERCENTAGE;
        }
        else if(event.getCpuUsage() >= ruleProperties.getCpu().getMajor()){
            severity = Severity.MAJOR;
            message = AlertMessage.CPU_THRESHOLD_EXCEEDED + ruleProperties.getCpu().getMajor() + AlertMessage.PERCENTAGE;
        }
        else if(event.getCpuUsage() >= ruleProperties.getCpu().getWarning()){
            severity = Severity.WARN;
            message = AlertMessage.CPU_THRESHOLD_EXCEEDED + ruleProperties.getCpu().getWarning() + AlertMessage.PERCENTAGE;
        }
        else {
            return Optional.empty();
        }
        return Optional.of(
                new AlertMetadata(
                    AlertType.CPU,
                    severity,
                    message
                )
        );
    }
}
