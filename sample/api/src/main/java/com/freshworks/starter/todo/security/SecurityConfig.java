package com.freshworks.starter.todo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityConfig {
    private Map<String, String[]> secrets;

    @Value("${security.jwt.expirationTime:864000000}")
    private long expirationTime;

    public Map<String, String[]> getSecrets() {
        return secrets;
    }

    public void setSecrets(Map<String, String[]> secrets) {
        this.secrets = secrets;
    }

    public String[] getJwtSecrets(String callerService) {
        return this.secrets.get(callerService);
    }

    public long getExpirationTime() {
        return expirationTime;
    }

}

