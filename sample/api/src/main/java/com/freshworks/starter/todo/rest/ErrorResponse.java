package com.freshworks.starter.todo.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.List;

public class ErrorResponse {
    private String code;
    private String description;
    private List<Error> errors;

    public ErrorResponse(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public ErrorResponse(String code, String description, Error... errors) {
        this.code = code;
        this.description = description;
        this.errors = Arrays.asList(errors);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Error> getErrors() {
        return errors;
    }

    public static class Error {
        private String field;
        private String code;
        private String message;

        public Error(String field, String code, String message) {
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", errors=" + errors +
                '}';
    }
}

