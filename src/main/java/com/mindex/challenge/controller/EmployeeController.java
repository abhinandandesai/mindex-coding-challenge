package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);
        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);
        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);
        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }


    // ### TASK 1 ###
    @GetMapping("/employee/{id}/reporting-structure")
    public ResponseEntity<ReportingStructure> getReportingStructure(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);
        ReportingStructure reportingStructure = employeeService.getReportingStructureByEmployeeId(id);
        if (reportingStructure == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid employeeId:");
        }

        return new ResponseEntity<>(reportingStructure, HttpStatus.OK);
    }
}
