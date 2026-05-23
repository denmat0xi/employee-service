package com.example.employeeservice.producer;

import com.example.employeeservice.event.EmployeeCreatedEvent;

public interface EmployeeEventProducer {
    void sendEmployeeCreatedEvent(EmployeeCreatedEvent event);
}