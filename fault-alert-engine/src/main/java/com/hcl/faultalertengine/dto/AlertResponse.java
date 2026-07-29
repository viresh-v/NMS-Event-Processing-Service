package com.hcl.faultalertengine.dto;

import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {

    private Long id;
    private String deviceId;
    private String ipAddress;
    private String alertType;
    private Severity severity;
    private String message;
    private AlertStatus status;
    private Integer occurenceCount;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime resolvedTime;

}
