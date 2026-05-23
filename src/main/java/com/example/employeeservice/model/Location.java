package com.example.employeeservice.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Location {
    @Embedded
    private Street street;
    private String city;
    private String state;
    private String country;
    private String postcode;

    @Embedded
    private Coordinates coordinates;

    @Embedded
    private Timezone timezone;
}