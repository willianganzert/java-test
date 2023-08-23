package com.sicred.votacoop.controllers.advice;

import com.sicred.votacoop.exceptions.BusinessException;
import com.sicred.votacoop.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Error retrieving voting status from the user service.", HttpStatus.BAD_GATEWAY);
        ResponseEntity<String> response = handler.handleBusinessException(ex);

        assertEquals(ex.getHttpStatus(), response.getStatusCode());
        assertEquals(ex.getMessage(), response.getBody());
    }

    @Test
    public void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        ObjectError error = new ObjectError("title", "Topic title cannot be null.");
        when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(ex.getBindingResult().getAllErrors()).thenReturn(Collections.singletonList(error));

        ResponseEntity<String> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(error.getDefaultMessage(), response.getBody());
    }

    @Test
    public void testHandleConstraintViolationException() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);
        when(violation.getMessage()).thenReturn("Topic name must be between 1 and 255 characters.");
        when(ex.getConstraintViolations()).thenReturn(violations);

        ResponseEntity<String> response = handler.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Topic name must be between 1 and 255 characters.", response.getBody());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Topic not found");
        ResponseEntity<String> response = handler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Topic not found", response.getBody());
    }
}
