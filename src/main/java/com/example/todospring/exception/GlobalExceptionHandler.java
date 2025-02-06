package com.example.todospring.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid annotations
     * Triggered when request body or parameters fail validation constraints
     * Returns 400 Bad Request with detailed field-level validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiError.ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> ApiError.ValidationError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .build())
            .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
            .message("Validation failed")
            .errors(validationErrors)
            .build();
        
        return ResponseEntity.badRequest().body(apiError);
    }

    /**
     * Handles cases when a requested resource is not found in the database
     * Typically triggered by findById() operations when ID doesn't exist
     * Returns 404 Not Found without body
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    /**
     * Handles malformed JSON or invalid request body format
     * Triggered when request body can't be deserialized to Java object
     * Returns 400 Bad Request with general error about request format
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        ApiError apiError = ApiError.builder()
            .message("Invalid request body")
            .errors(Collections.singletonList(
                ApiError.GeneralError.builder()
                    .message("Request body is malformed or missing")
                    .build()))
            .build();
        
        return ResponseEntity.badRequest().body(apiError);
    }

    /**
     * Handles type conversion errors in request parameters
     * Example: when a string is provided where a number is expected
     * Returns 400 Bad Request with details about the invalid parameter
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = ApiError.builder()
            .message("Invalid parameter type")
            .errors(Collections.singletonList(
                ApiError.GeneralError.builder()
                    .message("Invalid value for " + ex.getName())
                    .build()))
            .build();
        
        return ResponseEntity.badRequest().body(apiError);
    }

    /**
     * Catch-all handler for any unhandled exceptions
     * Acts as a safety net for unexpected errors
     * Returns 500 Internal Server Error with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllOtherExceptions(Exception ex) {
        ApiError apiError = ApiError.builder()
            .message("Internal server error")
            .errors(Collections.singletonList(
                ApiError.GeneralError.builder()
                    .message("An unexpected error occurred")
                    .build()))
            .build();
        
        return ResponseEntity.internalServerError().body(apiError);
    }
}