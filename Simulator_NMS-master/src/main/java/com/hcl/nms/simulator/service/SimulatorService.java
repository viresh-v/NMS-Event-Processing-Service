package com.hcl.nms.simulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcl.nms.simulator.config.SimulatorProperties;
import com.hcl.nms.simulator.generator.DeviceMetricGenerator;
import com.hcl.nms.simulator.model.DeviceMetric;
import com.hcl.nms.simulator.producer.DeviceProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimulatorService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SimulatorService.class);

    private final DeviceMetricGenerator generator;

    private final DeviceProducer producer;

    private final SimulatorProperties properties;

    public void simulateDevices() {

        LOGGER.info("Starting simulation of {} devices",
                properties.getDeviceCount());

        for (int i = 1; i <= properties.getDeviceCount(); i++) {

            DeviceMetric metric = generator.generateMetric(i);

            producer.publish(metric);

        }

        LOGGER.info("Completed simulation of {} devices",
                properties.getDeviceCount());

    }

}