package com.example.employeeservice.service;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.exception.EmployeeAlreadyExistsException;
import com.example.employeeservice.exception.EmployeeNotFoundException;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing employee business logic.
 * Provides methods for creating and retrieving employee records while ensuring
 * data integrity and providing traceability through logging.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Creates a new employee after validating that no duplicate exists by FML and email.
     * <p>
     * This method performs the following steps:
     * 1. Logs the attempt to create an employee (masking sensitive PII via Logback).
     * 2. Checks for existing records in the database using repository methods.
     * 3. Throws {@link EmployeeAlreadyExistsException} if a conflict is detected and logs a warning.
     * 4. Persists the new employee entity.
     * 5. Logs the successful creation with the generated ID.
     *
     * @param dto the employee data transfer object containing input details
     * @return the saved {@link Employee} entity
     * @throws EmployeeAlreadyExistsException if an employee with the same name already exists
     */
    @Transactional
    public Employee createEmployee(EmployeeRequestDTO dto) {
        log.info("Attempting to create a new employee with email: {}", dto.email());

        boolean exists = employeeRepository.countByName(
                dto.lastName(),
                dto.firstName(),
                dto.middleName()
        ) > 0;

        if (exists) {
            log.warn("Failed to create employee: Employee with name '{} {}' already exists",
                    dto.lastName(), dto.firstName());

            throw new EmployeeAlreadyExistsException(
                    String.format("Employee with full name '%s %s %s' already exists",
                                    dto.lastName(),
                                    dto.firstName(),
                                    dto.middleName() != null ? dto.middleName() : "")
                            .replaceAll("\\s+", " ").trim()
            );
        }

        Employee employee = Employee.builder()
                .title(dto.title())
                .lastName(dto.lastName())
                .firstName(dto.firstName())
                .middleName(dto.middleName())
                .email(dto.email())
                .phone(dto.phone())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        return savedEmployee;
    }

    /**
     * Retrieves an employee by their unique identifier.
     * <p>
     * Behavior:
     * 1. Logs the search request for auditing purposes.
     * 2. Queries the database for the specified ID.
     * 3. Throws {@link EmployeeNotFoundException} if the resource does not exist.
     *
     * @param id the database ID of the employee
     * @return the {@link Employee} entity if found
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    public Employee getEmployeeById(Long id) {
        log.info("Fetching employee details for ID: {}", id);

        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee retrieval failed: ID {} not found", id);
                    return new EmployeeNotFoundException("Employee with id " + id + " not found");
                });
    }
}