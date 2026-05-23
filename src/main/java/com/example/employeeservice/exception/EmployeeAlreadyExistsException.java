package com.example.employeeservice.exception;

/**
 * Exception thrown when an attempt is made to create an employee
 * that already exists in the system.
 */
public class EmployeeAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new EmployeeAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}