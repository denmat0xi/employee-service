package com.example.employeeservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisteredDTO(LocalDateTime date, Integer age) {}
