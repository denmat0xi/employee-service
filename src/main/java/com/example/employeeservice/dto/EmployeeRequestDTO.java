package com.example.employeeservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Data transfer object for incoming employee creation requests.
 */
@Builder
public record EmployeeRequestDTO(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Last name (Surname) is required")
        String lastName,

        @NotBlank(message = "First name is required")
        String firstName,

        String middleName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        String phone
) {

    /**
     * Constructs an immutable EmployeeRequestDTO instance.
     */
    public EmployeeRequestDTO(String title, String lastName, String firstName, String middleName, String email, String phone) {
        this.title = title;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.email = email;
        this.phone = phone;
    }
}