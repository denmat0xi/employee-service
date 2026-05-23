package com.example.employeeservice.service;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.event.EmployeeCreatedEvent;
import com.example.employeeservice.exception.EmployeeAlreadyExistsException;
import com.example.employeeservice.exception.EmployeeNotFoundException;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.producer.EmployeeEventProducer;
import com.example.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Core service layer for managing employee lifecycle and business logic.
 * <p>
 * This service acts as the orchestrator for employee operations, ensuring data integrity,
 * auditability, and event-driven communication with downstream microservices.
 * <p>
 * <b>Responsibilities:</b>
 * <ul>
 *     <li>Validation of employee data constraints (e.g., uniqueness).</li>
 *     <li>Safe audit logging with automated PII masking.</li>
 *     <li>Synchronous database persistence via {@link EmployeeRepository}.</li>
 *     <li>Asynchronous event propagation to Kafka via {@link EmployeeEventProducer}.</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeEventProducer eventProducer;

    /**
     * Creates and persists a new employee record.
     * Process Workflow:
     *     Logs the registration attempt (sensitive data is masked by MaskingConverter).
     *     Performs uniqueness validation using countByName.
     *     Persists the new {@link Employee} entity within a {@code @Transactional} context.
     *     Publishes an {@link EmployeeCreatedEvent} to the Kafka topic {@code employee.service.employeeData}.
     *
     *
     * @param dto the transfer object containing employee details (title, names, contact info)
     * @return the successfully persisted {@link Employee} entity with generated ID
     * @throws EmployeeAlreadyExistsException if a record with the same full name already exists
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
            throw new EmployeeAlreadyExistsException("Employee already exists");
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

        eventProducer.sendEmployeeCreatedEvent(new EmployeeCreatedEvent(
                savedEmployee.getId(),
                savedEmployee.getEmail(),
                LocalDateTime.now()
        ));

        return savedEmployee;
    }

    /**
     * Retrieves an employee record by their unique primary key.
     * This method logs every retrieval attempt for audit purposes. If the employee
     * is not found, an {@link EmployeeNotFoundException} is thrown.
     *
     * @param id the database identifier of the employee
     * @return the requested {@link Employee} entity
     * @throws EmployeeNotFoundException if no record corresponds to the given ID
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