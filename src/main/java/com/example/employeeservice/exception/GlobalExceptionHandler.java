package com.example.employeeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler that intercepts exceptions across the application
 * and maps them to standardized HTTP error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EmployeeAlreadyExistsException and returns a 409 Conflict status.
     *
     * @param ex the intercepted exception
     * @return a ResponseEntity containing the structured error details
     */
    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeAlreadyExists(EmployeeAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles EmployeeNotFoundException when a requested employee does not exist.
     * Maps the exception to a 404 Not Found status.
     *
     * @param ex the exception instance
     * @return a ResponseEntity containing error details and HTTP 404
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                "NOT_FOUND"
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}