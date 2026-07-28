package com.hcl.nms.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class DeviceSimulatorApplication {

    public static void main(String[] args) {
   //   SpringApplication.run(DeviceSimulatorApplication.class, args);
    	  SpringApplication app = new SpringApplication(DeviceSimulatorApplication.class);
          app.setAdditionalProfiles("local");
          app.run(args);
    }
}