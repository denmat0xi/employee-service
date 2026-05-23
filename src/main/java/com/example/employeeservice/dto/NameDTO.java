package com.example.employeeservice.dto;

import lombok.Builder;

@Builder
public record NameDTO(String title, String first, String last, String middle) {}
