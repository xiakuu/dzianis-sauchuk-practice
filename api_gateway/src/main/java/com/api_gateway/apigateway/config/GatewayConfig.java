package com.api_gateway.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {
    @Bean
    public WebClient authClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8983")
                .build();
    }

    @Bean
    public WebClient userClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8981")
                .build();
    }

}
