package com.hcl.nms.simulator.util;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class RandomValueGenerator {

    /**
     * Generates CPU usage between 0 and 100.
     */
    public double generateCpuUsage() {

        return Math.round(
                ThreadLocalRandom.current()
                        .nextDouble(0.0, 100.0) * 100.0) / 100.0;
    }

    /**
     * Generates Memory usage between 0 and 100.
     */
    public double generateMemoryUsage() {

        return Math.round(
                ThreadLocalRandom.current()
                        .nextDouble(0.0, 100.0) * 100.0) / 100.0;
    }

    /**
     * Generates Active Connections.
     */
    public int generateActiveConnections() {

        return ThreadLocalRandom.current()
                .nextInt(0, 1001);

    }

}