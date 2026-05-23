package com.example.employeeservice.service;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.exception.EmployeeAlreadyExistsException;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing employee business logic.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Creates a new employee in the system after validating uniqueness.
     *
     * @param dto the employee creation data transfer object
     * @return the saved Employee entity
     * @throws EmployeeAlreadyExistsException if an employee with the same full name already exists
     */
    @Transactional
    public Employee createEmployee(EmployeeRequestDTO dto) {
        boolean exists = employeeRepository.existsByFirstNameAndLastNameAndTitle(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getTitle()
        );

        if (exists) {
            throw new EmployeeAlreadyExistsException(
                    String.format("Employee with name '%s %s %s' already exists",
                            dto.getTitle(), dto.getFirstName(), dto.getLastName())
            );
        }

        Employee employee = Employee.builder()
                .title(dto.getTitle())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();

        return employeeRepository.save(employee);
    }
}