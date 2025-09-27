package com.notelm.ai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai.qwen")
public class AIProperties {

    private String apiUrl;
    private String modelName;
    private String apiKey;
    private String timeout = "60s";
    private Integer maxTokens = 2000;
    private Double temperature = 0.7;
}