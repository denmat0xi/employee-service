package com.example.employeeservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_fml",
                        columnNames = {"title", "first_name", "last_name", "middle_name"}
                )
        }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Location location;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "dob_date")),
            @AttributeOverride(name = "age", column = @Column(name = "dob_age"))
    })
    private Dob dob;

    @Embedded
    private Picture picture; // НОВОЕ: фото (large, medium, thumbnail)

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "registered_date")),
            @AttributeOverride(name = "age", column = @Column(name = "registered_age"))
    })
    private RegistrationInfo registered; // НОВОЕ: дата регистрации и возраст в системе

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "id_name")),
            @AttributeOverride(name = "value", column = @Column(name = "id_value"))
    })
    private IdentityInfo identityInfo; // НОВОЕ: документ (TFN и т.д.)

    @Column(nullable = false)
    private String email;

    private String phone;

    private String cell; // НОВОЕ: второй телефон

    private String gender;

    private String nat; // НОВОЕ: национальность

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}