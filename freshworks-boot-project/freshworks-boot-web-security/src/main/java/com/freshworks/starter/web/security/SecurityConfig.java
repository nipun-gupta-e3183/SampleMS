package com.freshworks.starter.web.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityConfig {
    private Map<String, String> authzPublicKeys;

    public Map<String, String> getAuthzPublicKeys() {
        return authzPublicKeys;
    }

    public void setAuthzPublicKeys(Map<String, String> authzPublicKeys) {
        this.authzPublicKeys = authzPublicKeys;
    }

    public String getPublicKey(String kid) {
        return this.authzPublicKeys.get(kid);
    }

}

