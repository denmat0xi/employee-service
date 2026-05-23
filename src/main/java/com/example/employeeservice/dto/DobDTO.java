package com.example.employeeservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DobDTO(LocalDateTime date, Integer age) {}
