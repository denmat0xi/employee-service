package com.example.employeeservice.dto;

import lombok.Builder;

@Builder
public record CoordinatesDTO(String latitude, String longitude) {}
