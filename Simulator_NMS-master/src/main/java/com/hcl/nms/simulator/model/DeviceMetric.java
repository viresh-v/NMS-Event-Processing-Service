package com.hcl.nms.simulator.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMetric {

	/*
	 * public String getDeviceId() { return deviceId; }
	 * 
	 * 
	 * public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
	 * 
	 * 
	 * public String getIpAddress() { return ipAddress; }
	 * 
	 * 
	 * public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
	 * 
	 * 
	 * public double getCpuUsage() { return cpuUsage; }
	 * 
	 * 
	 * public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
	 * 
	 * 
	 * public double getMemoryUsage() { return memoryUsage; }
	 * 
	 * 
	 * public void setMemoryUsage(double memoryUsage) { this.memoryUsage =
	 * memoryUsage; }
	 * 
	 * 
	 * public int getActiveConnections() { return activeConnections; }
	 * 
	 * 
	 * public void setActiveConnections(int activeConnections) {
	 * this.activeConnections = activeConnections; }
	 * 
	 * 
	 * public LocalDateTime getTimestamp() { return timestamp; }
	 * 
	 * 
	 * public void setTimestamp(LocalDateTime timestamp) { this.timestamp =
	 * timestamp; }
	 */


	@NotBlank(message = "Device Id is mandatory")
    private String deviceId;

    @NotBlank(message = "IP Address is mandatory")
    private String ipAddress;

    @Min(value = 0, message = "CPU Usage cannot be negative")
    @DecimalMax(value = "100.0", message = "CPU Usage cannot exceed 100")
    private double cpuUsage;

    @Min(value = 0, message = "Memory Usage cannot be negative")
    @DecimalMax(value = "100.0", message = "Memory Usage cannot exceed 100")
    private double memoryUsage;

    @Min(value = 0)
    @Max(value = 100000)
    private int activeConnections;

   
    @NotNull(message = "Timestamp cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;


	@Override
	public String toString() {
		return "DeviceMetric [deviceId=" + deviceId + ", ipAddress=" + ipAddress + ", cpuUsage=" + cpuUsage
				+ ", memoryUsage=" + memoryUsage + ", activeConnections=" + activeConnections + ", timestamp="
				+ timestamp + "]";
	}


	}

