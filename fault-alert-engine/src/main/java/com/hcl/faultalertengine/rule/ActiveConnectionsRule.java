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
public class ActiveConnectionsRule implements Rule{

    private final RuleProperties ruleProperties;

    public ActiveConnectionsRule(RuleProperties ruleProperties) {
        this.ruleProperties = ruleProperties;
    }

    @Override
    public Optional<AlertMetadata> evaluate(DeviceEvent event){
        Severity severity = null;
        String message = null;
        if(event.getActiveConnections() >= ruleProperties.getConnection().getCritical()){
            severity = Severity.CRITICAL;
            message = AlertMessage.ACTIVE_CONNECTIONS_EXCEEDED + ruleProperties.getConnection().getCritical() + AlertMessage.PERCENTAGE;
        }
        else if(event.getActiveConnections() >= ruleProperties.getConnection().getMajor()){
            severity = Severity.MAJOR;
            message = AlertMessage.ACTIVE_CONNECTIONS_EXCEEDED + ruleProperties.getConnection().getMajor() + AlertMessage.PERCENTAGE;
        }
        else if(event.getActiveConnections() >= ruleProperties.getConnection().getWarning()){
            severity = Severity.WARN;
            message = AlertMessage.ACTIVE_CONNECTIONS_EXCEEDED + ruleProperties.getConnection().getWarning() + AlertMessage.PERCENTAGE;
        }
        else {
            return Optional.empty();
        }
        return Optional.of(
            new AlertMetadata(
                    AlertType.ACTIVE_CONNECTIONS,
                    severity,
                    message
            )
        );
    }
}
