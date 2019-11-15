package com.freshworks.starter.web.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableConfigurationProperties(SecurityConfig.class)
@ConditionalOnMissingBean(WebSecurity.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder, SecurityConfig securityConfig) {
        return new UserDetailsServiceImpl(bCryptPasswordEncoder, securityConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSecurityConfigurerAdapter webSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, SecurityConfig securityConfig, PermissionsDecoder permissionsDecoder) {
        return new WebSecurity(userDetailsService, bCryptPasswordEncoder, securityConfig, permissionsDecoder);
    }
}
