package com.conexa.test.dev.backend.exception;

import com.conexa.test.dev.backend.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = SWApiException.class)
    protected ResponseEntity<ApiError> handleConflict(SWApiException ex) {
        ApiError apiError = new ApiError(ex.getStatusCode(), ex.getMessage(), ex.getOriginalException());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
