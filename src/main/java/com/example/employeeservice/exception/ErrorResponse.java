package com.example.employeeservice.exception;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Data transfer object representing a standardized error structure
 * returned to clients in case of an exception.
 */
@Getter
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final LocalDateTime timestamp;

    /**
     * Constructs a new ErrorResponse.
     *
     * @param status    the HTTP status code
     * @param error     the error description or reason phrases
     * @param message   the detailed error message
     */
    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}