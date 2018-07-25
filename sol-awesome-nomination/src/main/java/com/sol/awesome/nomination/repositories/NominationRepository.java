package com.sol.awesome.nomination.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sol.awesome.nomination.domain.Nomination;

public interface NominationRepository extends PagingAndSortingRepository<Nomination, Long> {

}
