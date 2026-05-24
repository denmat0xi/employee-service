package com.example.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;


@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
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

