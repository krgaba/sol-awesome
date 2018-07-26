package com.sol.awesome.nomination.repositories;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sol.awesome.nomination.domain.Nomination;

public interface NominationRepository extends PagingAndSortingRepository<Nomination, Long> {

	Page<Nomination> findByEmployeeId(Long id, Pageable pageParams);

	Page<Nomination> findByDateBetween(Date from, Date to, Pageable pageParams);
}
