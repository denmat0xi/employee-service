package com.example.employeeservice.dto;

import lombok.Builder;

@Builder
public record TimezoneDTO(String offset, String description) {}
