package com.notelm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NoteLM 学习平台 API")
                        .version("1.0")
                        .description("NoteLM 学习平台后端API文档")
                        .contact(new Contact()
                                .name("NoteLM Team")
                                .email("notelm@example.com")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("notelm-api")
                .pathsToMatch("/**")
                .build();
    }
}