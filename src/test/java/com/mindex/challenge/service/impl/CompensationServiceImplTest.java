package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompensationServiceImplTest {
    @Autowired
    private CompensationService compensationService;

    @Mock
    private CompensationRepository compensationRepository;

    private Employee employee;
    private Compensation compensation;

    @Before
    public void setup() {
        employee = new Employee();

        employee.setEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3");
        employee.setFirstName("Paul");
        employee.setLastName("McCartney");
        employee.setDepartment("Engineering");
        employee.setPosition("Developer I");

        compensation = new Compensation(employee, 100000.0, LocalDate.now());
    }

    @Test
    public void testCreate() {
        List<Compensation> compensations = compensationService.create(Collections.singletonList(compensation));
        assertNotNull(compensations.get(0).getEmployee().getEmployeeId());
        assertEmployeeEquivalence(employee, compensations.get(0).getEmployee());
    }

    @Test
    public void testRead() {
        when(compensationRepository.employeeSearch_ID(anyString())).thenReturn(compensation);
        Compensation compensationRes = compensationService.read("b7839309-3348-463b-a7e3-5de1c168beb3");
        assertNotNull(compensationRes.getEmployee().getEmployeeId());
        assertEmployeeEquivalence(employee, compensationRes.getEmployee());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetReportingStructureByEmployeeIdInvalidEmployeeId() {
        when(compensationRepository.employeeSearch_ID(anyString())).thenReturn(null);
        compensationService.read("Invalid Employee ID");
    }
}