package com.hcl.faultalertengine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rules")
public class RuleProperties {

    private Cpu cpu = new Cpu();
    private Memory memory = new Memory();
    private Connection connection = new Connection();

    @Data
    public static class Cpu {
        private Double warning;
        private Double major;
        private Double critical;
    }

    @Data
    public static class Memory {
        private Double warning;
        private Double major;
        private Double critical;
    }

    @Data
    public static class Connection {
        private Double warning;
        private Double major;
        private Double critical;
    }
}
