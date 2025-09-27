package com.notelm.config;

import com.notelm.ai.AIProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AIProperties.class)
public class AIConfig {
    // AI相关配置，主要通过AIProperties进行配置属性绑定
}