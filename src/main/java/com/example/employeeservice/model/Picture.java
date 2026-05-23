package com.example.employeeservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Picture {
    private String large;
    private String medium;
    private String thumbnail;
}