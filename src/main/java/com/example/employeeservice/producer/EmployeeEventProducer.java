package com.example.employeeservice.producer;

import com.example.employeeservice.event.EmployeeCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Producer responsible for publishing employee-related events to Kafka topics.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "employee.service.employeeData";

    /**
     * Publishes an {@link EmployeeCreatedEvent} to the Kafka topic.
     *
     * @param event the event to be published
     */
    public void sendEmployeeCreatedEvent(EmployeeCreatedEvent event) {
        log.info("Publishing EmployeeCreatedEvent for ID: {}", event.employeeId());

        kafkaTemplate.send(TOPIC, String.valueOf(event.employeeId()), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Event sent successfully to topic: {}", TOPIC);
                    } else {
                        log.error("Failed to send event: {}", ex.getMessage());
                    }
                });
    }
}