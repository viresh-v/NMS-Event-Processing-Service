package com.hcl.nms.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "simulator")
public class SimulatorProperties {

    /**
     * Kafka Topic Name
     */
    private String topicName;

    public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public int getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}

	public long getPublishInterval() {
		return publishInterval;
	}

	public void setPublishInterval(long publishInterval) {
		this.publishInterval = publishInterval;
	}

	public String getDevicePrefix() {
		return devicePrefix;
	}

	public void setDevicePrefix(String devicePrefix) {
		this.devicePrefix = devicePrefix;
	}

	/**
     * Number of devices generated in one batch
     */
    private int deviceCount;

    /**
     * Scheduler interval (milliseconds)
     */
    private long publishInterval;

    /**
     * Device prefix
     * Example : DEVICE-001
     */
    private String devicePrefix;

	@Override
	public String toString() {
		return "SimulatorProperties [topicName=" + topicName + ", deviceCount=" + deviceCount + ", publishInterval="
				+ publishInterval + ", devicePrefix=" + devicePrefix + "]";
	}
}