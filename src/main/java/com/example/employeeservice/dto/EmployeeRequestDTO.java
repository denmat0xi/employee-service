package com.example.employeeservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmployeeRequestDTO(
        NameDTO name,
        LocationDTO location,
        String email,
        String phone,
        String cell,
        DobDTO dob,
        RegisteredDTO registered,
        String gender,
        String nat,
        PictureDTO picture,
        IdDTO id
) {}

