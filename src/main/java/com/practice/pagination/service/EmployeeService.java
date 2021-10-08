package com.practice.pagination.service;

import com.practice.pagination.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(long id) throws Exception;
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Employee employee) throws Exception;
    void deleteEmployee(long id) throws Exception;
    Page<Employee> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection);
}
