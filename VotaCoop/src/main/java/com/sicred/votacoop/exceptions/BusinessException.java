package com.sicred.votacoop.exceptions;

import jakarta.validation.ConstraintViolationException;

public class BusinessException extends ConstraintViolationException {
    public BusinessException(String message) {
        super(message, null);
    }
}