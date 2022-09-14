package com.abnamro.recipe.advice;

import com.abnamro.recipe.api.model.ErrorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiControllerAdviceTests {

    private static final String MESSAGE_ERROR = "No found resource!";

    ApiControllerAdvice handler;

    @BeforeEach
    public void setUp() {
        handler = new ApiControllerAdvice();
    }

    @Test
    void test_handleException_should_have_BAD_REQUEST_status() {
        Exception ex = new Exception(MESSAGE_ERROR);
        ResponseEntity<ErrorContext> responseEntity = handler.handleException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void test_handleIllegalArgumentException_should_have_BAD_REQUEST_status() {
        IllegalArgumentException ex = new IllegalArgumentException(MESSAGE_ERROR);
        ResponseEntity<ErrorContext> responseEntity = handler.handleException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
