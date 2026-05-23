package com.example.employeeservice.exception;

/**
 * Exception thrown when an employee is not found in the database.
 */
public class EmployeeNotFoundException extends RuntimeException {

    /**
     * Constructs a new EmployeeNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}