package com.freshworks.boot.rest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestAutoConfiguration {
//TODO: When configured with AutoConfiguration, ErrorAttributes is picked from ErrorMvcAutoConfiguration.
// To circumvent this issue, currently the api application has to include this package in scan package.
/*
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
*/

    @ConditionalOnMissingBean(ModelMapper.class)
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
