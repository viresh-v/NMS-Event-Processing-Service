package com.hcl.faultalertengine.service;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.enums.Severity;
import com.hcl.faultalertengine.rule.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleEngineServiceTest {

    @Mock
    private AlertService alertService;

    @Mock
    private Rule cpuRule;

    @Mock
    private Rule memoryRule;

    @InjectMocks
    private RuleEngineService ruleEngineService;

    private DeviceEvent event;

    @BeforeEach
    void setUp(){
        event = DeviceEvent.builder()
                .deviceId("DEVICE-001")
                .cpuUsage(90.0)
                .memoryUsage(85.0)
                .activeConnections(600.0)
                .build();

        List<Rule> rules = List.of(cpuRule, memoryRule);
        ruleEngineService = new RuleEngineService(alertService, rules);
    }

    @Test
    void shouldResolvedRecoveredAlertWhenNoRulesMatch(){
        when(cpuRule.evaluate(any()))
                .thenReturn(Optional.empty());

        when(memoryRule.evaluate(any()))
                .thenReturn(Optional.empty());

        Optional<List<AlertMetadata>> result = ruleEngineService.evaluateRules(event);
        assertTrue(result.isEmpty());
        verify(alertService).resolveRecoveredAlerts(eq(event), eq(Collections.emptySet()));
        verify(alertService, never()).processAlert(any(), any());
    }

    @Test
    void shouldProcessSingleAlert(){
        AlertMetadata cpuAlert = AlertMetadata.builder()
                        .alertType("CPU")
                        .severity(Severity.CRITICAL)
                        .message("CPU exceeded")
                        .build();

        when(cpuRule.evaluate(any()))
                .thenReturn(Optional.of(cpuAlert));

        when(memoryRule.evaluate(any()))
                .thenReturn(Optional.empty());

        Optional<List<AlertMetadata>> result = ruleEngineService.evaluateRules(event);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());

        verify(alertService).processAlert(event, cpuAlert);
        verify(alertService).resolveRecoveredAlerts(eq(event), eq(Set.of("CPU")));
    }

    @Test
    void shouldProcessMultipleAlerts(){
        AlertMetadata cpuAlert = AlertMetadata.builder()
                .alertType("CPU")
                .severity(Severity.CRITICAL)
                .build();

        AlertMetadata memoryAlert = AlertMetadata.builder()
                .alertType("MEMORY")
                .severity(Severity.MAJOR)
                .build();

        when(cpuRule.evaluate(any()))
                .thenReturn(Optional.of(cpuAlert));

        when(memoryRule.evaluate(any()))
                .thenReturn(Optional.of(memoryAlert));

        Optional<List<AlertMetadata>> result = ruleEngineService.evaluateRules(event);
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());

        verify(alertService, times(2)).processAlert(any(), any());
    }

    @Test
    void shouldEvaluateEveryRule(){
        when(cpuRule.evaluate(any()))
                .thenReturn(Optional.empty());

        when(memoryRule.evaluate(any()))
                .thenReturn(Optional.empty());

        ruleEngineService.evaluateRules(event);
        verify(cpuRule).evaluate(event);
        verify(memoryRule).evaluate(event);
    }

}
