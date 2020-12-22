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

        employee.setEmployeeId("01abc234-frt9-5294-923a-zyx12341qwer");
        employee.setFirstName("Will");
        employee.setLastName("Smith");
        employee.setDepartment("Computer Science");
        employee.setPosition("Developer");

        compensation = new Compensation(employee,  130000.0, LocalDate.now());
    }

    @Test
    public void testPostCompensationSuccess() throws Exception {
        final String link = url;
        String exampleEntry = "[{\"employee\": {\n" +
                "      \"employeeId\" : \"ab111222-2345-3ab4-c3d4-5ef6g789hi00\",\n" +
                "      \"firstName\" : \"ABC\",\n" +
                "      \"lastName\" : \"XYZ\",\n" +
                "      \"position\" : \"Developer II\",\n" +
                "      \"department\" : \"Computer Engineering\"\n" +
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
        final String link = url + "/01abc234-frt9-5294-923a-zyx12341qwer";

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
        final String link = url + "/invalid-path/01abc234-frt9-5294-923a-zyx12341qwer";

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