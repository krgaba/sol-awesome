package com.sol.awesome.nomination;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sol.awesome.nomination.domain.AwesomeEmployee;
import com.sol.awesome.nomination.domain.Nomination;
import com.sol.awesome.nomination.domain.SolsticePrinciple;

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

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreate() throws Exception {
		createNomination();
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

	private Nomination createNomination() throws Exception {
		Nomination nomination = nominationTemplate();
		ResultActions ra = mvc
				.perform(post(nominationPath).content(toJson(nomination)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201));

		Nomination savedNomination = toDomainObject(ra, Nomination.class);
		assertNotNull(savedNomination.getId());
		// assertNotNull(savedNomination.getFirstName());
		// assertNotNull(savedNomination.getEmail());
		// assertNotEquals(nomination.getId(), savedNomination.getId());
		return savedNomination;
	}

	private Nomination nominationTemplate() {
		Nomination nomination = new Nomination();
		AwesomeEmployee employee = new AwesomeEmployee() {{
			setId(120L);
			setFirstName("Khurum");
			setLastName("Gaba");
		}};
		AwesomeEmployee nominatedBy = new AwesomeEmployee() {{
			setId(140L);
			setFirstName("Alexander");
			setLastName("Bronshtein");
		}};
		nomination.setEmployee(employee);
		nomination.setNominatedBy(nominatedBy);
		nomination.setPrincipleGroup(
				Arrays.stream(SolsticePrinciple.values()).map(p -> p.name()).collect(Collectors.joining(",")));
		nomination.setDescription("We are tired today");

		return nomination;
	}

}
