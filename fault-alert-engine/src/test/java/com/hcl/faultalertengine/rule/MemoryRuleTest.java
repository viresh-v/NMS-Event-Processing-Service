package com.hcl.faultalertengine.rule;

import com.hcl.faultalertengine.config.RuleProperties;
import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.enums.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemoryRuleTest {

    private MemoryRule memoryRule;

    @BeforeEach
    void setUp() {
        RuleProperties properties = new RuleProperties();
        properties.getMemory().setWarning(70.0);
        properties.getMemory().setMajor(85.0);
        properties.getMemory().setCritical(95.0);
        memoryRule = new MemoryRule(properties);
    }

    private DeviceEvent createEvent(int memoryUsage) {
        return DeviceEvent.builder()
                .deviceId("DEVICE-001")
                .cpuUsage(50.0)
                .memoryUsage((double) memoryUsage)
                .activeConnections(100.0)
                .build();
    }

    @Test
    void shouldGenerateAlertWhenMemoryExceedsThreshold() {

        Optional<AlertMetadata> result = memoryRule.evaluate(createEvent(90));

        assertTrue(result.isPresent());
        assertEquals("MEMORY", result.get().getAlertType());
        assertEquals(Severity.MAJOR, result.get().getSeverity());
    }

    @Test
    void shouldNotGenerateAlertWhenMemoryBelowThreshold() {

        Optional<AlertMetadata> result = memoryRule.evaluate(createEvent(60));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGenerateAlertWhenMemoryEqualsThreshold() {

        Optional<AlertMetadata> result = memoryRule.evaluate(createEvent(75));

        assertTrue(result.isPresent());
    }
}