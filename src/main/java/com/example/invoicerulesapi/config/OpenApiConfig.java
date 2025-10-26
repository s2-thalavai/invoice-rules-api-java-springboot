package com.example.invoicerulesapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        // Main global API metadata
        @Bean
        public OpenAPI invoiceRulesOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Invoice Rules API")
                                                .description("Spring Boot REST API for managing invoices and dynamic business rules.")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("Sivasankar Thalavai")
                                                                .email("developer@example.com")
                                                                .url("https://www.example.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("GitHub Repository")
                                                .url("https://github.com/example/invoice-rules-api"));
        }

        // Group 1: Invoices API
        @Bean
        public GroupedOpenApi invoicesGroup() {
                return GroupedOpenApi.builder()
                                .group("invoices")
                                .pathsToMatch("/invoices/**")
                                .build();
        }

        // Group 2: Rules Engine API
        @Bean
        public GroupedOpenApi rulesGroup() {
                return GroupedOpenApi.builder()
                                .group("rules")
                                .pathsToMatch("/rules/**")
                                .build();
        }

        // Group 3: System / Health
        @Bean
        public GroupedOpenApi systemGroup() {
                return GroupedOpenApi.builder()
                                .group("system")
                                .pathsToMatch("/actuator/**")
                                .build();
        }
}
