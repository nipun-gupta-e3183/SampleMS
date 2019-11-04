package com.freshworks.starter.sample.api.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class StandardErrorAttributes extends DefaultErrorAttributes {
    private Logger log = LoggerFactory.getLogger(StandardErrorAttributes.class);
    private static final Map<Integer, String[]> STATUS_TO_ERROR_MAP = Map.ofEntries(
            Map.entry(404, new String[]{"not_found", "The resource is not found"}),
            Map.entry(500, new String[]{"internal_server_error", "Something went wrong. Please try again after some time or contact support."})
    );

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        // Let Spring handle the error first, we will modify later :)
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        log.error("Some unhandled exception occurred: {}", errorAttributes);
        Integer status = (Integer) errorAttributes.get("status");
        if (STATUS_TO_ERROR_MAP.containsKey(status)) {
            return Map.of(
                    "code", STATUS_TO_ERROR_MAP.get(status)[0],
                    "description", STATUS_TO_ERROR_MAP.get(status)[1]
            );
        }
        return Map.of(
                "code", "internal_server_error",
                "description", "Something went wrong. Please try again after some time or contact support."
                );
    }

}
