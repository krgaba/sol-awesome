package com.sol.awesome.nomination.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sol.awesome.nomination.domain.Nomination;
import com.sol.awesome.nomination.service.NominationService;

@RestController("/nominations")
public class NominationController {
	private final NominationService nominationService;

	public NominationController(NominationService nominationService) {
		this.nominationService = nominationService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Nomination create(@RequestBody @Valid Nomination request) {
		return nominationService.create(request);
	}

}
