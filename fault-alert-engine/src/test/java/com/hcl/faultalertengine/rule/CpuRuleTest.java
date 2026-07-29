package com.hcl.faultalertengine.rule;

import com.hcl.faultalertengine.config.RuleProperties;
import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.enums.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CpuRuleTest {

    private CpuRule cpuRule;

    @BeforeEach
    void setUp() {
        RuleProperties properties = new RuleProperties();
        properties.getCpu().setWarning(70.0);
        properties.getCpu().setMajor(80.0);
        properties.getCpu().setCritical(90.0);
        cpuRule = new CpuRule(properties);
    }

    private DeviceEvent createEvent(int cpuUsage) {
        return DeviceEvent.builder()
                .deviceId("DEVICE-001")
                .cpuUsage((double) cpuUsage)
                .memoryUsage(50.0)
                .activeConnections(100.0)
                .build();
    }

    @Test
    void shouldGenerateAlertWhenCpuExceedsThreshold() {

        Optional<AlertMetadata> result = cpuRule.evaluate(createEvent(90));

        assertTrue(result.isPresent());
        assertEquals("CPU", result.get().getAlertType());
        assertEquals(Severity.CRITICAL, result.get().getSeverity());
    }

    @Test
    void shouldNotGenerateAlertWhenCpuBelowThreshold() {

        Optional<AlertMetadata> result = cpuRule.evaluate(createEvent(60));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGenerateAlertWhenCpuEqualsThreshold() {

        Optional<AlertMetadata> result = cpuRule.evaluate(createEvent(80));

        // Change this if your rule uses >
        assertTrue(result.isPresent());
    }
}