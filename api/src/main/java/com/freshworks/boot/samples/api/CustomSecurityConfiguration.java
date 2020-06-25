package com.freshworks.boot.samples.api;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomSecurityConfiguration implements com.freshworks.starter.web.security.SecurityConfiguration {
    @Override
    public Map<HttpMethod, String[]> unauthenticatedPathPatterns() {
        return Map.of(HttpMethod.GET, new String[]{"/api/v1/about"});
    }
}
