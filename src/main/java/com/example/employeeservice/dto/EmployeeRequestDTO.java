package com.example.employeeservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

/**
 * Data transfer object for incoming employee creation requests.
 * Contains validation constraints for required fields.
 */
@Getter
@Builder
public final class EmployeeRequestDTO {

    @NotBlank(message = "Title is required")
    private final String title;

    @NotBlank(message = "First name is required")
    private final String firstName;

    @NotBlank(message = "Last name is required")
    private final String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private final String email;

    private final String phone;

    /**
     * Constructs an immutable EmployeeRequestDTO instance.
     */
    public EmployeeRequestDTO(String title, String firstName, String lastName, String email, String phone) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}