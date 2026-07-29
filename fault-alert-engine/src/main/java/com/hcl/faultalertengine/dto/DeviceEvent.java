package com.hcl.faultalertengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEvent {

    private String deviceId;
    private String ipAddress;
    private Double cpuUsage;
    private Double memoryUsage;
    private Double activeConnections;
    private String status;
    private LocalDateTime eventTime;

}
