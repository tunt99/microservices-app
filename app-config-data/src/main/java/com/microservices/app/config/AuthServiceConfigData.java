package com.microservices.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth-service")
public class AuthServiceConfigData {
    private String version;
    private String customAudience;
}
