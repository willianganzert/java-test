package com.sicred.votacoop.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;

public class BusinessException extends ConstraintViolationException {
    private final HttpStatus httpStatus;
    public BusinessException(String message, HttpStatus httpStatus) {
        super(message, null);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}