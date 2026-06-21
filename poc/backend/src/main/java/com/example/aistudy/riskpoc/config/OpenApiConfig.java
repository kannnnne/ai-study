package com.example.aistudy.riskpoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI riskPocOpenApi() {
        return new OpenAPI().info(new Info()
                .title("AI Study Risk POC API")
                .version("1.0.0")
                .description("第一阶段 Spring Boot 单体后端 API"));
    }

    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder().group("customer-api").pathsToMatch("/api/customers", "/api/customers/*").build();
    }

    @Bean
    public GroupedOpenApi accountApi() {
        return GroupedOpenApi.builder().group("account-api").pathsToMatch("/api/customers/*/accounts").build();
    }

    @Bean
    public GroupedOpenApi riskApi() {
        return GroupedOpenApi.builder().group("risk-api").pathsToMatch("/api/customers/*/risk/**", "/api/customers/*/risk-records").build();
    }

    @Bean
    public GroupedOpenApi operationLogApi() {
        return GroupedOpenApi.builder().group("operation-log-api").pathsToMatch("/api/operation-logs").build();
    }
}
