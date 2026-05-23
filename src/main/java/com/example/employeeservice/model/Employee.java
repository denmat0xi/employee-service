package com.example.employeeservice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an employee.
 * All fields are immutable after creation to satisfy business requirements.
 */
@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_fml",
                        columnNames = {"title", "first_name", "last_name", "middle_name"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Constructs a new Employee instance with current timestamp.
     *
     * @param title     the title prefix (e.g., Mr, Ms, Dr)
     * @param firstName the first name of the employee
     * @param middleName the patronymic of the employee
     * @param lastName  the last name of the employee
     * @param email     the email address of the employee
     * @param phone     the phone number of the employee
     */
    @Builder
    public Employee(String title, String firstName, String middleName, String lastName, String email, String phone) {
        this.title = title;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.createdAt = LocalDateTime.now();
    }
}