package com.example.employeeservice.controller;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing employee-related endpoints.
 */
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Creates a new employee.
     *
     * @param dto the validated employee creation data
     * @return ResponseEntity containing the saved Employee and HTTP 201 Created status
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        Employee createdEmployee = employeeService.createEmployee(dto);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    /**
     * Fetches details of a specific employee by ID.
     *
     * @param id the ID of the employee to retrieve
     * @return a ResponseEntity containing the employee data and a 200 OK status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}