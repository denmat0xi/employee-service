package com.example.employeeservice.controller;

import com.example.employeeservice.dto.EmployeeListResponse;
import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeListResponse wrapper) {
        log.info("Received request to create employee");

        if (wrapper.results() == null || wrapper.results().isEmpty()) {
            log.warn("Received empty results list");
            return ResponseEntity.badRequest().build();
        }

        EmployeeRequestDTO dto = wrapper.results().get(0);
        Employee createdEmployee = employeeService.createEmployee(dto);

        log.info("Employee created successfully with ID: {}", createdEmployee.getId());
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        log.info("Fetching employee with ID: {}", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}