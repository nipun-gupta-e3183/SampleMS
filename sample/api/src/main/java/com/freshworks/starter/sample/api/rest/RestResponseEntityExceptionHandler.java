package com.freshworks.starter.sample.api.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(
            ValidationException ex, WebRequest request) {
        log.debug("Validation error occurred", ex);
        ErrorResponse response = new ErrorResponse("validation_failed",
                "One or more fields didn't meet the constraints",
                new ErrorResponse.Error(ex.getField(), ex.getErrorCode(), ex.getErrorMessage()));
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

/*
TODO: Handle more error scenarios
    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(ConstraintViolationException ex, WebRequest request) throws IOException {
        List<ErrorResponse.Error> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> new ErrorResponse.Error(x.getField(), x.getCode(), x.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("validation_failed",
                "One or more fields didn't meet the constraints",
                errors);
        return new ResponseEntity<>(errorResponse, headers, status);
    }
*/

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        log.debug("AccessDeniedException", ex);
        ErrorResponse response = new ErrorResponse("access_denied",
                "You don't have permission to access this resource");
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        ErrorResponse.Error[] errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> new ErrorResponse.Error(x.getField(), x.getCode(), x.getDefaultMessage()))
                .toArray(ErrorResponse.Error[]::new);

        ErrorResponse errorResponse = new ErrorResponse("validation_failed",
                "One or more fields didn't meet the constraints",
                errors);
        return new ResponseEntity<>(errorResponse, headers, status);

    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex, WebRequest request) {
        log.debug("NotFoundException", ex);
        ErrorResponse response = new ErrorResponse("not_found",
                "The resource is not found",
                new ErrorResponse.Error(ex.getField(), "not_found", "The resource is not found"));
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleServerErrors(
            Exception ex, WebRequest request) {
        log.error("Unexpected exception occurred", ex);
        ErrorResponse response = new ErrorResponse("internal_server_error",
                "Something went wrong. Please try again after some time or contact support.");
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}