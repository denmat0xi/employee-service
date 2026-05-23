package com.example.employeeservice.dto;

import java.time.LocalDateTime;

/**
 * Standardized error response structure for API exceptions.
 *
 * @param message   the error description
 * @param status    the HTTP status code
 * @param timestamp the time when the error occurred
 */
public record ErrorResponse(String message, int status, LocalDateTime timestamp) {
}