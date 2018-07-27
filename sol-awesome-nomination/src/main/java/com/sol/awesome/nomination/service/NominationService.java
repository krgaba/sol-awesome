package com.sol.awesome.nomination.service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.awesome.nomination.client.AwesomeEmployeeClient;
import com.sol.awesome.nomination.domain.AwesomeEmployee;
import com.sol.awesome.nomination.domain.Nomination;
import com.sol.awesome.nomination.repositories.NominationRepository;

@Service
@Transactional
public class NominationService {
	private static final int WEEK_NUM_MAX = 53;
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
		
		if (CollectionUtils.isEmpty(employees)) {
			throw new IllegalArgumentException("Could not find employee from " + request);
		}
		return employees;

	}

	public Page<Nomination> getNominationsForEmployee(Long id, Integer pageNumber, Integer pageSize) {

		return nominationRepository.findByEmployeeId(id, PageRequest.of(pageNumber, pageSize));

	}

	public Page<Nomination> getForDateRange(Date from, Date to, Integer pageNumber, Integer pageSize) {
		return nominationRepository.findByDateBetween(from, to, PageRequest.of(pageNumber, pageSize));

	}

	public Page<Nomination> getForWeek(Integer weekNum, Integer pageNumber, Integer pageSize) {
		Map.Entry<Date, Date> datePair = fromWeek(weekNum);
		return getForDateRange(datePair.getKey(), datePair.getValue(), pageNumber, pageSize);
	}

	private Entry<Date, Date> fromWeek(Integer weekNum) {
		LocalDate now = LocalDate.now();
		
		if ((weekNum != null) && (weekNum > 0) && (weekNum < WEEK_NUM_MAX)) {
			now = now
		            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNum);
		} 
		
		LocalDate from = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate to = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		return new AbstractMap.SimpleImmutableEntry<>(Date.valueOf(from), Date.valueOf(to));
	}

}
