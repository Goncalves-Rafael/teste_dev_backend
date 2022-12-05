package com.conexa.test.dev.backend.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SWApiException extends Exception {

    private String message;
    private Throwable originalException;
    private HttpStatus statusCode;

    public SWApiException(String message, Throwable ex) {
        this.message = message;
        this.originalException = ex;
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public SWApiException(String message, Throwable ex, HttpStatus status) {
        super();
        this.message = message;
        this.originalException = ex;
        this.statusCode = status;
    }
}
