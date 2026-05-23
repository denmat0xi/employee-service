package com.example.employeeservice.repository;

import com.example.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Employee entities in the database.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Checks if an employee already exists by their full name combination.
     *
     * @param firstName the first name of the employee
     * @param lastName  the last name of the employee
     * @param title     the title prefix of the employee
     * @return true if a matching record exists, false otherwise
     */
    boolean existsByFirstNameAndLastNameAndTitle(String firstName, String lastName, String title);
}