package com.freshworks.starter.sample.common.jpa;

public class NotFoundException extends RuntimeException {
    private String field;

    public NotFoundException(String field) {
        super("Entity not found");
        this.field = field;
    }

    public NotFoundException(String field, Throwable cause) {
        super("Entity not found", cause);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
