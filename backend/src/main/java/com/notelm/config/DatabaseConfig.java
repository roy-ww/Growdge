package com.notelm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.notelm.repository")
public class DatabaseConfig {
    // 数据库配置
}