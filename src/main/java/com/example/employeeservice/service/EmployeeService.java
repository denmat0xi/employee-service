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
     * Creates a new employee after validating that no duplicate exists by FML and email.
     *
     * @param dto the employee data transfer object
     * @return the saved Employee entity
     * @throws EmployeeAlreadyExistsException if an employee with the same FMl or email already exists
     */
    @Transactional
    public Employee createEmployee(EmployeeRequestDTO dto) {
        boolean exists = employeeRepository.countByName(
                dto.lastName(),
                dto.firstName(),
                dto.middleName()
                ) > 0;

        if (exists) {
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

        return employeeRepository.save(employee);
    }
}