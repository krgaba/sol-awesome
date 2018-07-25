package com.sol.awesome.nomination.service;


import java.sql.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.awesome.nomination.client.AwesomeEmployeeClient;
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
		request.setDate(new Date(System.currentTimeMillis()));
		Nomination saved = nominationRepository.save(request);
		
		return saved;
		
	}
	
	

}
