package com.example.employeeservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Name {
    private String title;
    @Column(name = "first_name")
    private String first;
    @Column(name = "last_name")
    private String last;
    @Column(name = "middle_name")
    private String middle;
}