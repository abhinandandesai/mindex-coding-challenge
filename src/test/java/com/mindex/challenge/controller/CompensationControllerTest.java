package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
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

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CompensationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CompensationService compensationService;

    private static final String url = "/compensation";

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

        compensation = new Compensation(employee,  130000.0, LocalDate.now());
    }

    @Test
    public void testPostCompensationSuccess() throws Exception {
        final String link = url;
        String exampleEntry = "[{\"employee\": {\n" +
                "      \"employeeId\" : \"62c1084e-6e34-4630-93fd-9153afb65309\",\n" +
                "      \"firstName\" : \"Pete\",\n" +
                "      \"lastName\" : \"Best\",\n" +
                "      \"position\" : \"Developer II\",\n" +
                "      \"department\" : \"Engineering\"\n" +
                "    },\n" +
                "    \"salary\": 65000,\n" +
                "    \"effectiveDate\": \"07/01/2019\"}]";

        when(compensationService.read(anyString())).thenReturn(compensation);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post(link)
                        .content(exampleEntry)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    public void testGetCompensationSuccess() throws Exception {
        final String link = url + "/62c1084e-6e34-4630-93fd-9153afb65309";

        when(compensationService.read(anyString())).thenReturn(compensation);

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
    public void testGetCompensationInvalidPath() throws Exception {
        final String link = url + "/invalid-path/62c1084e-6e34-4630-93fd-9153afb65309";

        when(compensationService.read(anyString())).thenReturn(compensation);

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
    public void testGetCompensationInvalidEmployeeId() throws Exception {
        final String link = url + "/12ac-34bd56-99999";

        when(compensationService.read(anyString())).thenReturn(null);

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