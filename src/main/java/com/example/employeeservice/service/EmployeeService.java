package com.example.employeeservice.service;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.event.EmployeeCreatedEvent;
import com.example.employeeservice.exception.EmployeeAlreadyExistsException;
import com.example.employeeservice.exception.EmployeeNotFoundException;
import com.example.employeeservice.model.*;
import com.example.employeeservice.producer.EmployeeEventProducer;
import com.example.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeEventProducer eventProducer;

    @Transactional
    public Employee createEmployee(EmployeeRequestDTO dto) {
        log.info("Attempting to create a new employee with email: {}", dto.email());

        // 1. Проверка уникальности (используем данные из вложенного объекта name)
        boolean exists = employeeRepository.countByName(
                dto.name().last(),
                dto.name().first(),
                dto.name().middle()
        ) > 0;

        if (exists) {
            throw new EmployeeAlreadyExistsException("Employee already exists");
        }

        // 2. Сборка объекта Employee (аккуратный маппинг)
        Employee employee = Employee.builder()
                .name(Name.builder()
                        .title(dto.name().title())
                        .first(dto.name().first())
                        .last(dto.name().last())
                        .middle(dto.name().middle())
                        .build())

                .location(Location.builder()
                        .street(Street.builder()
                                .number(dto.location().street().number())
                                .name(dto.location().street().name())
                                .build())
                        .city(dto.location().city())
                        .state(dto.location().state())
                        .country(dto.location().country())
                        .postcode(dto.location().postcode())
                        .coordinates(Coordinates.builder()
                                .latitude(dto.location().coordinates().latitude())
                                .longitude(dto.location().coordinates().longitude())
                                .build())
                        .timezone(Timezone.builder()
                                .offset(dto.location().timezone().offset())
                                .description(dto.location().timezone().description())
                                .build())
                        .build())

                .dob(Dob.builder()
                        .date(dto.dob().date())
                        .age(dto.dob().age())
                        .build())

                .registered(RegistrationInfo.builder()
                        .date(dto.registered().date())
                        .age(dto.registered().age())
                        .build())

                .picture(Picture.builder()
                        .large(dto.picture().large())
                        .medium(dto.picture().medium())
                        .thumbnail(dto.picture().thumbnail())
                        .build())

                .identityInfo(IdentityInfo.builder()
                        .name(dto.id().name())
                        .value(dto.id().value())
                        .build())

                .email(dto.email())
                .phone(dto.phone())
                .cell(dto.cell())
                .gender(dto.gender())
                .nat(dto.nat())
                .createdAt(LocalDateTime.now())
                .build();

        // 3. Сохранение
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        // 4. Событие
        eventProducer.sendEmployeeCreatedEvent(new EmployeeCreatedEvent(
                savedEmployee.getId(),
                savedEmployee.getEmail(),
                LocalDateTime.now()
        ));

        return savedEmployee;
    }

    public Employee getEmployeeById(Long id) {
        log.info("Fetching employee details for ID: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee retrieval failed: ID {} not found", id);
                    return new EmployeeNotFoundException("Employee with id " + id + " not found");
                });
    }
}