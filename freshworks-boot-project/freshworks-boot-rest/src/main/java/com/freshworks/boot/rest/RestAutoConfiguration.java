package com.freshworks.boot.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

//TODO: When configured with AutoConfiguration, ErrorAttributes is picked from ErrorMvcAutoConfiguration. Fix that
// To circumvent this issue, currently the api application has to include this package in scan package.
//@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestAutoConfiguration {
    @Bean
    public RestResponseEntityExceptionHandler restResponseEntityExceptionHandler() {
        return new RestResponseEntityExceptionHandler();
    }
    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorAttributes standardErrorAttributes() {
        return new StandardErrorAttributes();
    }
}
