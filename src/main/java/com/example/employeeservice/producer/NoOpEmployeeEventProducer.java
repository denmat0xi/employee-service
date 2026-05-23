package com.example.employeeservice.producer;

import com.example.employeeservice.event.EmployeeCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * No-operation producer used when Kafka is not available or disabled via profile.
 */
@Slf4j
@Component
@Profile("no-kafka") // Active ONLY IF profile no-kafka enabled
public class NoOpEmployeeEventProducer implements EmployeeEventProducer {

    @Override
    public void sendEmployeeCreatedEvent(EmployeeCreatedEvent event) {
        log.info("Kafka is disabled. Skipping event publication for ID: {}", event.employeeId());
    }
}