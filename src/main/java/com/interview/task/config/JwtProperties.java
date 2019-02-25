package com.interview.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class represents JWT properties.
 */
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secretKey;
    private Long expirationTimeMs;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getExpirationTimeMs() {
        return expirationTimeMs;
    }

    public void setExpirationTimeMs(Long expirationTimeMs) {
        this.expirationTimeMs = expirationTimeMs;
    }
}
