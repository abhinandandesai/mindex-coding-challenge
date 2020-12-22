package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService employeeService;

    private static final String url = "/employee";

    private Employee employee;
    private ReportingStructure reportingStructure;

    @Before
    public void setup() {
        employee = new Employee();

        employee.setEmployeeId("01abc234-frt9-5294-923a-zyx12341qwer");
        employee.setFirstName("Will");
        employee.setLastName("Smith");
        employee.setDepartment("Computer Science");
        employee.setPosition("Developer");

        reportingStructure = new ReportingStructure(employee, 2000);
    }

    @Test
    public void testGetReportingStructureSuccess() throws Exception {
        final String link = url + "/01abc234-frt9-5294-923a-zyx12341qwer/reporting-structure";

        when(employeeService.getReportingStructureByEmployeeId(anyString())).thenReturn(reportingStructure);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get(link)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testGetReportingStructureInvalidPath() throws Exception {
        final String link = url + "/01abc234-frt9-5294-923a-zyx12341qwer/reportingStructure";

        when(employeeService.getReportingStructureByEmployeeId(anyString())).thenReturn(reportingStructure);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get(link)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void testGetReportingStructureInvalidEmployeeId() throws Exception {
        final String link = url + "/12ac-34bd56-99999/reportingStructure";

        when(employeeService.getReportingStructureByEmployeeId(anyString())).thenReturn(null);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get(link)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}