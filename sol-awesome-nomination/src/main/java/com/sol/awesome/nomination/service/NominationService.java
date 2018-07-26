package com.sol.awesome.nomination.service;

import java.sql.Date;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.awesome.nomination.client.AwesomeEmployeeClient;
import com.sol.awesome.nomination.domain.AwesomeEmployee;
import com.sol.awesome.nomination.domain.Nomination;
import com.sol.awesome.nomination.repositories.NominationRepository;

@Service
@Transactional
public class NominationService {
	private final NominationRepository nominationRepository;
	private final AwesomeEmployeeClient awesomeEmployeeClient;

	public NominationService(NominationRepository nominationRepository, AwesomeEmployeeClient awesomeEmployeeClient) {
		this.nominationRepository = nominationRepository;
		this.awesomeEmployeeClient = awesomeEmployeeClient;
	}

	public Nomination create(Nomination request) {

		Collection<AwesomeEmployee> existingEmployees = findExistingEmployees(request);
		existingEmployees.forEach(e -> {
			final Long savedId = e.getId();
			if (savedId.equals(request.getEmployee().getId())) {
				request.setEmployee(e);
			} else if (savedId.equals(request.getNominatedBy().getId())) {
				request.setNominatedBy(e);
			} else {
				throw new IllegalArgumentException(String.format("Wrong saved employee %s for request %s", e, request));
			}
		});

		request.setDate(new Date(System.currentTimeMillis()));
		Nomination saved = nominationRepository.save(request);

		return saved;

	}

	private Collection<AwesomeEmployee> findExistingEmployees(Nomination request) {
		Set<Long> ids = Stream.<Long>builder().add(request.getEmployee().getId()).add(request.getNominatedBy().getId())
				.build().collect(Collectors.toSet());

		Collection<AwesomeEmployee> employees = awesomeEmployeeClient.getEmployees(ids).getContent();
		// TODO: validate size, throw exception if absent
		return employees;

	}


	public Page<Nomination> getNominationsForEmployee(Long id, Integer pageNumber, Integer pageSize) {
		
		return this.nominationRepository.findByEmployeeId(id, PageRequest.of(pageNumber, pageSize));

	}

}
