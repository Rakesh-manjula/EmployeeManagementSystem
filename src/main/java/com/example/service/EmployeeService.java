package com.example.service;

import com.example.entity.Employee;
import com.example.exception.DuplicateEmailException;
import com.example.exception.EmployeeNotFoundException;
import com.example.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // CREATE - Add Employee
    public Employee addEmployee(Employee employee) {

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateEmailException(
                    "Email " + employee.getEmail() + " already exists"
            );
        }

        return employeeRepository.save(employee);
    }

    // READ ALL - Get All Employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // READ BY ID - Get Employee By ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee with ID " + id + " not found"
                        ));
    }

    // UPDATE - Update Employee
    public Employee updateEmployee(Long id, Employee employee) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee with ID " + id + " not found"
                        ));

        // Check duplicate email during update
        if (!existingEmployee.getEmail().equals(employee.getEmail())
                && employeeRepository.existsByEmail(employee.getEmail())) {

            throw new DuplicateEmailException(
                    "Email " + employee.getEmail() + " already exists"
            );
        }

        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setSalary(employee.getSalary());

        return employeeRepository.save(existingEmployee);
    }

    // DELETE - Delete Employee
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee with ID " + id + " not found"
            );
        }

        employeeRepository.deleteById(id);
    }
}