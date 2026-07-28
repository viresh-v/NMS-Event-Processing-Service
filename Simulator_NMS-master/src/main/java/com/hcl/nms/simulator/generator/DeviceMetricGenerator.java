package com.hcl.nms.simulator.generator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hcl.nms.simulator.config.SimulatorProperties;
import com.hcl.nms.simulator.constants.SimulatorConstants;
import com.hcl.nms.simulator.model.DeviceMetric;
import com.hcl.nms.simulator.util.RandomValueGenerator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceMetricGenerator {

	private final SimulatorProperties simulatorProperties;

	private final RandomValueGenerator randomValueGenerator;

	public DeviceMetric generateMetric(int sequenceNumber) {

		double cpuUsage = randomValueGenerator.generateCpuUsage();

		double memoryUsage = randomValueGenerator.generateMemoryUsage();

		return DeviceMetric.builder().deviceId(generateDeviceId(sequenceNumber))
				.ipAddress(generateIpAddress(sequenceNumber)).cpuUsage(cpuUsage).memoryUsage(memoryUsage)
				.activeConnections(randomValueGenerator.generateActiveConnections())
				.timestamp(LocalDateTime.now()).build();

	}

	/**
	 * DEVICE-001
	 */
	private String generateDeviceId(int sequenceNumber) {

		return String.format(SimulatorConstants.DEVICE_ID_FORMAT, simulatorProperties.getDevicePrefix(),
				sequenceNumber);

	}

	/**
	 * 192.168.1.X
	 */
	private String generateIpAddress(int sequenceNumber) {

		int lastOctet = (sequenceNumber % 254) + 1;

		return SimulatorConstants.IP_PREFIX + lastOctet;

	}

	/**
	 * Health Calculation
	 *//*
		 * private DeviceHealth calculateHealth( double cpuUsage, double memoryUsage) {
		 * 
		 * if (cpuUsage >= 90 || memoryUsage >= 90) {
		 * 
		 * return DeviceHealth.DOWN;
		 * 
		 * }
		 * 
		 * if (cpuUsage >= 70 || memoryUsage >= 70) {
		 * 
		 * return DeviceHealth.WARNING;
		 * 
		 * }
		 * 
		 * return DeviceHealth.HEALTHY;
		 * 
		 * }
		 */
}