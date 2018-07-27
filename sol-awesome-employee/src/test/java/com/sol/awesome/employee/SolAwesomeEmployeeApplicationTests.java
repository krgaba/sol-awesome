package com.sol.awesome.employee;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sol.awesome.employee.domain.Employee;
import com.sol.awesome.employee.domain.Office;
import com.sol.awesome.employee.repositories.EmployeeRepository;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class SolAwesomeEmployeeApplicationTests {

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private MockMvc mvc;
	@Value("${employee_url:employees}")
	private String employeePath;
	private AtomicInteger employeeNumber = new AtomicInteger(12345);
	
	@Autowired
	EmployeeRepository repository;
	
	@After
	@Transactional
	public void tearDown() {
		
		repository.deleteAll();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void createAndGetEmployee() throws Exception {

		createEmployee("Khuram", "Gaba");
	}

	@Test
	public void createAndGetByIds() throws Exception {

		String ids = Arrays.asList(createEmployee("Khuram", "Gaba"), createEmployee("Alexander", "Bronshtein")).stream()
				.map(e -> String.valueOf(e.getId())).collect(Collectors.joining(","));

		mvc.perform(get("/" + employeePath + "/search/findByIdIn").param("ids", ids).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$._embedded.employees", hasSize(2)))
				.andExpect(jsonPath("$._embedded.employees[0].firstName", equalTo("Khuram")));

	}

	private Employee employeeTemplate(String firstname, String lastName) {
		return new Employee(null, firstname, lastName, String.valueOf(employeeNumber.incrementAndGet()), Office.Chicago, "Consultant", "kgaba@solstice.com",
				"http://testurl.com");
	}

	private Employee createEmployee(String firstname, String lastName) throws Exception {
		Employee employee = employeeTemplate(firstname, lastName);
		ResultActions ra = mvc.perform(post("/" + employeePath).content(toJson(employee))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201));

		Employee savedEmployee = toDomainObject(ra, Employee.class);
		assertNotNull(savedEmployee.getId());
		assertNotNull(savedEmployee.getFirstName());
		assertNotNull(savedEmployee.getEmail());
		assertNotEquals(employee.getId(), savedEmployee.getId());
		return savedEmployee;
	}

	@SuppressWarnings("unchecked")
	private <T> T toDomainObject(ResultActions resultActions, Class<T> domainClass) throws IOException {
		MvcResult result = resultActions.andReturn();
		String asString = result.getResponse().getContentAsString();
		return String.class == domainClass ? (T) asString : objectMapper.readValue(asString, domainClass);
	}

	public String toJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}

}
