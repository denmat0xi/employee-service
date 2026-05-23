package com.example.employeeservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record EmployeeListResponse(List<EmployeeRequestDTO> results) {
}