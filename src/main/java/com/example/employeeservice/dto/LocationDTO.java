package com.example.employeeservice.dto;

import lombok.Builder;

@Builder
public record LocationDTO(StreetDTO street, String city, String state, String country, String postcode, CoordinatesDTO coordinates, TimezoneDTO timezone) {}
