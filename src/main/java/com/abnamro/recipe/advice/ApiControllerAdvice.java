package com.abnamro.recipe.advice;

import com.abnamro.recipe.api.model.ErrorContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorContext> handleException(Exception exception) {
        return ResponseEntity.badRequest().body(errorContext(exception));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorContext> handleEIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(errorContext(exception));
    }

    private ErrorContext errorContext(String message) {
        ErrorContext context = new ErrorContext();
        context.setMessage(message);
        return context;
    }

    private ErrorContext errorContext(Exception exception) {
        return errorContext(exception.getMessage());
    }
}
