package com.practice.pagination.service;

import com.practice.pagination.model.Employee;
import com.practice.pagination.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) throws Exception {
        Optional<Employee> existingEmployee = this.employeeRepository.findById(id);

        if(existingEmployee.isPresent()) {
            return this.employeeRepository.findById(id);
        }

        throw new Exception("User does not exist.");
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) throws Exception{
        Employee existingEmployee = this.employeeRepository
                .findById(employee.getId())
                .orElseThrow(() -> new Exception("Employee with that ID does not exist."));

        return this.employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(long id) throws Exception {
        Employee existingEmployee = this.employeeRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Employee with that ID does not exist."));

        this.employeeRepository.delete(existingEmployee);
    }

    @Override
    public Page<Employee> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        // spring data jpa api consider a page number with zero base so we need to do (pageNumber - 1)
        // if we want support for both Sort and Pagination we need to pass in the sort a long with PageRequest
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return this.employeeRepository.findAll(pageable);
    }
}
