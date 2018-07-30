package com.sol.awesome.nomination;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sol.awesome.nomination.client.AwesomeEmployeeClient;
import com.sol.awesome.nomination.domain.AwesomeEmployee;
import com.sol.awesome.nomination.domain.Nomination;
import com.sol.awesome.nomination.domain.SolsticePrinciple;
import com.sol.awesome.nomination.repositories.NominationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class SolAwesomeNominationApplicationTests {

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private MockMvc mvc;
	private String nominationPath = "/nominations";
	@MockBean
	AwesomeEmployeeClient awesomeEmployeeClient;
	@Autowired
	NominationRepository repository;

	@After
	@Transactional
	public void tearDown() {
		repository.deleteAll();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreate() throws Exception {
		createNominationOk();
	}

	@Test
	public void testGetSingleNominationByEmployee() throws Exception {
		Nomination nomination1 = nominationTemplate();
		nomination1.getEmployee().setId(133L);
		Nomination nomination2 = nominationTemplate();
		nomination2.getEmployee().setId(134L);
		createNominationOk(nomination1);
		createNominationOk(nomination2);

		mvc.perform(get(nominationPath + "/employee/" + 133).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].employee.firstName", equalTo("Khurum")));

	}

	@Test
	public void testGetNominationsByDateRange() throws Exception {
		Nomination nomination1 = nominationTemplate();
		nomination1.getEmployee().setId(153L);
		Nomination nomination2 = nominationTemplate();
		nomination2.getEmployee().setId(154L);
		createNominationOk(nomination1);
		createNominationOk(nomination2);

		String fromDateStr = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String toDateStr = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

		mvc.perform(get(nominationPath + "/period/from/" + fromDateStr + "/to/" + toDateStr)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].employee.firstName", equalTo("Khurum")));

	}

	@Test
	public void testGetNominationsByOutOfDateRange() throws Exception {
		Nomination nomination1 = nominationTemplate();
		nomination1.getEmployee().setId(153L);
		Nomination nomination2 = nominationTemplate();
		nomination2.getEmployee().setId(154L);
		createNominationOk(nomination1);
		createNominationOk(nomination2);

		String fromDateStr = LocalDateTime.now().plusDays(-3).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String toDateStr = LocalDateTime.now().plusDays(-2).format(DateTimeFormatter.ISO_LOCAL_DATE);

		mvc.perform(get(nominationPath + "/period/from/" + fromDateStr + "/to/" + toDateStr)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.content", hasSize(0)));

	}

	@Test
	public void testGetNominationsByWeekAbsentWeekNum() throws Exception {
		Nomination nomination1 = nominationTemplate();
		nomination1.getEmployee().setId(153L);
		Nomination nomination2 = nominationTemplate();
		nomination2.getEmployee().setId(154L);
		createNominationOk(nomination1);
		createNominationOk(nomination2);

		mvc.perform(get(nominationPath + "/period/week/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].employee.firstName", equalTo("Khurum")));

	}

	//TODO: check logic on sunday @Test
	public void testGetNominationsByWeekWithNum() throws Exception {
		Nomination nomination1 = nominationTemplate();
		nomination1.getEmployee().setId(153L);
		Nomination nomination2 = nominationTemplate();
		nomination2.getEmployee().setId(154L);
		createNominationOk(nomination1);
		createNominationOk(nomination2);

		int currentWeekNum = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

		mvc.perform(get(nominationPath + "/period/week/").param("weekNum", String.valueOf(currentWeekNum))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].employee.firstName", equalTo("Khurum")));

	}

	@SuppressWarnings("unchecked")
	private <T> T toDomainObject(ResultActions resultActions, Class<T> domainClass) throws IOException {
		MvcResult result = resultActions.andReturn();
		String asString = result.getResponse().getContentAsString();
		return String.class == domainClass ? (T) asString : objectMapper.readValue(asString, domainClass);
	}

	public String toJson(Object o) throws JsonProcessingException {
		return objectMapper.writeValueAsString(o);
	}

	private Nomination createNominationOk() throws Exception {
		return createNominationOk(nominationTemplate());
	}

	private Nomination createNominationOk(Nomination nomination) throws Exception {
		List<AwesomeEmployee> employees = Arrays.asList(nomination.getEmployee(), nomination.getNominatedBy());
		Set<Long> ids = employees.stream().map(AwesomeEmployee::getId).collect(Collectors.toSet());
		Mockito.when(awesomeEmployeeClient.getEmployees(ids)).thenReturn(new Resources<>(employees));
		ResultActions ra = mvc
				.perform(post(nominationPath).content(toJson(nomination)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201));

		Nomination savedNomination = toDomainObject(ra, Nomination.class);
		assertNotNull(savedNomination.getId());
		assertNotEquals(nomination.getId(), savedNomination.getId());
		return savedNomination;
	}

	private Nomination nominationTemplate() {
		Nomination nomination = new Nomination();
		AwesomeEmployee employee = new AwesomeEmployee() {
			{
				setId(120L);
				setFirstName("Khurum");
				setLastName("Gaba");
			}
		};
		AwesomeEmployee nominatedBy = new AwesomeEmployee() {
			{
				setId(140L);
				setFirstName("Alexander");
				setLastName("Bronshtein");
			}
		};
		nomination.setEmployee(employee);
		nomination.setNominatedBy(nominatedBy);
		nomination.setPrincipleGroup(
				Arrays.stream(SolsticePrinciple.values()).map(p -> p.name()).collect(Collectors.joining(",")));
		nomination.setDescription("We are tired today");

		return nomination;
	}

}
