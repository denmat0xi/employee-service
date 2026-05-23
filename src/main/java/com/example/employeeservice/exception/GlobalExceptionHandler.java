package com.example.employeeservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler that intercepts exceptions thrown by controllers or services
 * and maps them to standardized {@link ErrorResponse} objects.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepts {@link EmployeeAlreadyExistsException}.
     * <p>
     * Behavior: Returns an HTTP 409 Conflict status. Used when business logic detects
     * an attempt to create a duplicate resource.
     *
     * @param ex the exception containing the conflict details
     * @return a {@link ResponseEntity} with status 409 and error body
     */
    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeAlreadyExists(EmployeeAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Intercepts {@link EmployeeNotFoundException}.
     * <p>
     * Behavior: Returns an HTTP 404 Not Found status. Used when a client requests
     * a resource that does not exist in the database.
     *
     * @param ex the exception containing the not found details
     * @return a {@link ResponseEntity} with status 404 and error body
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Intercepts {@link DataIntegrityViolationException}.
     * <p>
     * Behavior: Returns an HTTP 409 Conflict status. Acts as a safety net for
     * database-level constraint violations (e.g., unique key conflicts) that
     * are not caught by application-level validation.
     *
     * @param ex the database exception
     * @return a {@link ResponseEntity} with status 409 and generic error message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseConflict(DataIntegrityViolationException ex) {
        return buildErrorResponse("Database integrity constraint violation", HttpStatus.CONFLICT);
    }

    /**
     * Intercepts all generic {@link Exception} types.
     * <p>
     * Behavior: Returns an HTTP 500 Internal Server Error status. This is a
     * "catch-all" handler to ensure that raw stack traces or internal implementation
     * details are never exposed to the client.
     *
     * @param ex the unhandled exception
     * @return a {@link ResponseEntity} with status 500 and a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        // Here you could add logger.error("Unexpected error:", ex);
        return buildErrorResponse("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Private helper method to encapsulate the creation of the {@link ResponseEntity}.
     * <p>
     * Behavior: Standardizes the error response structure, ensuring that every
     * error returned by the API has a consistent format (timestamp, status, reason, message).
     *
     * @param message the descriptive error message
     * @param status  the HTTP status to return
     * @return a constructed {@link ResponseEntity}
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(error, status);
    }
}