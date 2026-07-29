package com.nms.eventprocessing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device_events")
public class DeviceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    @Column(name = "ip_address", nullable = false, length = 100)
    private String ipAddress;

    @Column(name = "cpu_usage", nullable = false)
    private Double cpuUsage;

    @Column(name = "memory_usage", nullable = false)
    private Double memoryUsage;

    @Column(name = "active_connections", nullable = false)
    private Integer activeConnections;

    @Column(name = "timestamp", nullable = false, length = 50)
    private String timestamp;

    protected DeviceEvent() {
    }

    public DeviceEvent(
            String deviceId,
            String ipAddress,
            Double cpuUsage,
            Double memoryUsage,
            Integer activeConnections,
            String timestamp) {

        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.activeConnections = activeConnections;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public Integer getActiveConnections() {
        return activeConnections;
    }

    public String getTimestamp() {
        return timestamp;
    }
}