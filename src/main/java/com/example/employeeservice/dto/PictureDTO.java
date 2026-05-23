package com.example.employeeservice.dto;

import lombok.Builder;

@Builder
public record PictureDTO(String large, String medium, String thumbnail) {}
