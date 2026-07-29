package com.hcl.faultalertengine.rule;

import com.hcl.faultalertengine.config.RuleProperties;
import com.hcl.faultalertengine.constants.AlertType;
import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.enums.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ActiveConnectionsRuleTest {

    private ActiveConnectionsRule activeConnectionsRule;

    @BeforeEach
    void setUp() {

        RuleProperties properties = new RuleProperties();

        properties.getConnection().setWarning(500.0);
        properties.getConnection().setMajor(700.0);
        properties.getConnection().setCritical(900.0);

        activeConnectionsRule =
                new ActiveConnectionsRule(properties);
    }

    private DeviceEvent createEvent(double connections) {

        return DeviceEvent.builder()
                .deviceId("DEVICE-001")
                .cpuUsage(50.0)
                .memoryUsage(50.0)
                .activeConnections(connections)
                .build();
    }

    @Test
    void shouldGenerateWarningAlert() {

        Optional<AlertMetadata> result =
                activeConnectionsRule.evaluate(createEvent(500));

        assertTrue(result.isPresent());

        assertEquals(AlertType.ACTIVE_CONNECTIONS,
                result.get().getAlertType());

        assertEquals(Severity.WARN,
                result.get().getSeverity());

    }

    @Test
    void shouldGenerateMajorAlert() {

        Optional<AlertMetadata> result =
                activeConnectionsRule.evaluate(createEvent(700));

        assertTrue(result.isPresent());

        assertEquals(AlertType.ACTIVE_CONNECTIONS,
                result.get().getAlertType());

        assertEquals(Severity.MAJOR,
                result.get().getSeverity());

    }

    @Test
    void shouldGenerateCriticalAlert() {

        Optional<AlertMetadata> result =
                activeConnectionsRule.evaluate(createEvent(900));

        assertTrue(result.isPresent());

        assertEquals(AlertType.ACTIVE_CONNECTIONS,
                result.get().getAlertType());

        assertEquals(Severity.CRITICAL,
                result.get().getSeverity());

    }

    @Test
    void shouldNotGenerateAlertBelowWarningThreshold() {

        Optional<AlertMetadata> result =
                activeConnectionsRule.evaluate(createEvent(499));

        assertTrue(result.isEmpty());

    }

}