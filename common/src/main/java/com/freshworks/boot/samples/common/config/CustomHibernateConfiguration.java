package com.freshworks.boot.samples.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CustomHibernateConfiguration implements HibernatePropertiesCustomizer {

    @Autowired
    MultiTenantHibernateInterceptor multiTenantHibernateInterceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
            hibernateProperties.put("hibernate.ejb.interceptor", multiTenantHibernateInterceptor);
    }
}
