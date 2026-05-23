package com.example.employeeservice.repository;

import com.example.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Employee entity connections.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Checks if an employee exists by their full name (FML).
     *
     * @param lastName   the family name
     * @param firstName  the given name
     * @param middleName the patronymic or middle name
     * @return num of duplicates in db
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.lastName = :lastName AND e.firstName = :firstName AND e.middleName = :middleName")
    Long countByName(@Param("lastName") String lastName, @Param("firstName") String firstName, @Param("middleName") String middleName);
}