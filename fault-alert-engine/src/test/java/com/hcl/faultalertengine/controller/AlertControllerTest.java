package com.hcl.faultalertengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.faultalertengine.dto.AlertResponse;
import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import com.hcl.faultalertengine.exception.AlertNotFoundException;
import com.hcl.faultalertengine.exception.InvalidAlertStateException;
import com.hcl.faultalertengine.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertService alertService;

    private AlertResponse response;

    @BeforeEach
    void setUp() {
        response = AlertResponse.builder()
                .id(1L)
                .deviceId("DEVICE-001")
                .alertType("CPU")
                .severity(Severity.CRITICAL)
                .status(AlertStatus.OPEN)
                .message("CPU threshold exceeded")
                .build();
    }

    @Test
    void shouldReturnAlertById() throws Exception {

        when(alertService.getAlertById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/alerts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.deviceId")
                        .value("DEVICE-001"))
                .andExpect(jsonPath("$.data.alertType")
                        .value("CPU"));
    }

    @Test
    void shouldReturnAllAlerts() throws Exception {

        when(alertService.getAllAlerts())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.length()")
                        .value(1));
    }

    @Test
    void shouldReturnOpenAlerts() throws Exception {

        when(alertService.getOpenAlerts())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/alerts/open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()")
                        .value(1));
    }

    @Test
    void shouldAcknowledgeAlert() throws Exception {

        mockMvc.perform(put("/api/v1/alerts/1/acknowledge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true));

        verify(alertService)
                .acknowledgeAlert(1L);
    }

    @Test
    void shouldResolveAlert() throws Exception {

        mockMvc.perform(put("/api/v1/alerts/1/resolve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true));

        verify(alertService)
                .resolveAlert(1L);
    }

    @Test
    void shouldReturn404WhenAlertNotFound() throws Exception {

        when(alertService.getAlertById(1L))
                .thenThrow(new AlertNotFoundException(1L));

        mockMvc.perform(get("/api/v1/alerts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidAlertState() throws Exception {

        doThrow(new InvalidAlertStateException(
                "Alert already resolved"))
                .when(alertService)
                .acknowledgeAlert(1L);

        mockMvc.perform(put("/api/v1/alerts/1/acknowledge"))
                .andExpect(status().isBadRequest());
    }
}
