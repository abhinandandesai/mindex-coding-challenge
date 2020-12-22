package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }


    // ### TASK 1 ###

    @Override
    public ReportingStructure getReportingStructureByEmployeeId(String id) {
        LOG.debug("Get employee reporting structure for id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid employeeId");
        }

        int totalDirectReports = calculateReports(Collections.singletonList(employee), 0);

        return new ReportingStructure(employee, totalDirectReports);
    }

    /**
     * A function that uses recursion to find the reports.
     *
     * @param employees list of employees and their desciptive data
     * @param directReportsCount direct reports
     * @return finalCount all people that report to the employee
     */
    private int calculateReports(List<Employee> employees, int directReportsCount) {
        List<Employee> directReports = new ArrayList<>();

        for (Employee employee : employees) {
            if (employee.getDirectReports() != null) {
                List<String> reportees = employee.getDirectReports().stream()
                        .map(Employee::getEmployeeId)
                        .collect(Collectors.toList());

                List<Employee> reports = new ArrayList<>((List<Employee>) employeeRepository.findAllById(reportees));
                employee.setDirectReports(reports);
                directReports.addAll(reports);
                directReportsCount += reports.size();
            }
        }
        int finalCount = directReportsCount;
        if (directReports.size() > 0) return calculateReports(directReports, finalCount);
        return finalCount;
    }
}
