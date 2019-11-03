package com.freshworks.starter.todo.rest;

public class NotFoundException extends RuntimeException {
    private String field;

    public NotFoundException(String field) {
        super("Resource not found");
        this.field = field;
    }

    public NotFoundException(String field, Throwable cause) {
        super("Resource not found", cause);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
