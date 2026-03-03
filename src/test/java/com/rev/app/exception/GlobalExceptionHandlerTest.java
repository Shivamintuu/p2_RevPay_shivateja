package com.rev.app.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFoundException_Returns404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<GlobalExceptionHandler.ErrorDetails> response = handler.handleResourceNotFoundException(ex);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", response.getBody().getDetails());
        assertEquals("Not found", response.getBody().getMessage());
    }

    @Test
    void handleBadRequestException_Returns400() {
        BadRequestException ex = new BadRequestException("Bad request");
        ResponseEntity<GlobalExceptionHandler.ErrorDetails> response = handler.handleBadRequestException(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BAD_REQUEST", response.getBody().getDetails());
        assertEquals("Bad request", response.getBody().getMessage());
    }
}
