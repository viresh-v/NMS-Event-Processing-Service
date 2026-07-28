package com.nms.eventprocessing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventProcessingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Processing Service API")
                        .description("REST APIs for retrieving processed tower health events.")
                        .version("v1"));
    }
}
