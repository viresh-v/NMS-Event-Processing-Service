package com.hcl.nms.simulator.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hcl.nms.simulator.service.SimulatorService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceScheduler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DeviceScheduler.class);

    private  final SimulatorService simulatorService;

    @Scheduled(
            fixedRateString =
                    "${simulator.publish-interval}")
    public void publishMetrics() {

        LOGGER.info("Scheduler triggered");

        simulatorService.simulateDevices();

    }

}