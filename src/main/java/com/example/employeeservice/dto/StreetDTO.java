package com.example.employeeservice.dto;

import lombok.Builder;

@Builder
public record StreetDTO(Integer number, String name) {}
