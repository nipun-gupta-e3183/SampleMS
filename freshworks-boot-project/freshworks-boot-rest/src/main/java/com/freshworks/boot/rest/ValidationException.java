package com.freshworks.boot.rest;

@SuppressWarnings("WeakerAccess")
public class ValidationException extends RuntimeException {
    private int status;
    private String errorCode;
    private String errorMessage;
    private String field;

    public ValidationException(int status, String errorCode, String errorMessage, String field) {
        super(errorCode);
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.field = field;
    }

    public ValidationException(int status, String errorCode, String errorMessage, String field, Throwable cause) {
        super(errorCode, cause);
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.field = field;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getField() {
        return field;
    }
}
