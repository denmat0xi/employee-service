package com.example.employeeservice.event;

import java.time.LocalDateTime;

/**
 * Event published when a new employee is successfully created.
 *
 * @param employeeId the ID of the created employee
 * @param email      the email of the employee
 * @param timestamp  the time when the event occurred
 */
public record EmployeeCreatedEvent(Long employeeId, String email, LocalDateTime timestamp) {
}