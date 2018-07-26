package com.sol.awesome.nomination.controller;

import java.sql.Date;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sol.awesome.nomination.domain.Nomination;
import com.sol.awesome.nomination.service.NominationService;

@RestController
@RequestMapping("/nominations")
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
	
	@GetMapping(path="/employee/{id}")
	public @ResponseBody Page<Nomination> getNominationsForEmployee(@PathVariable("id") Long id, 
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "30") Integer pageSize ) {
		return nominationService.getNominationsForEmployee(id, pageNumber, pageSize);
	}
	
	@GetMapping(path="/period/from/{from}/to/{to}")
	public @ResponseBody Page<Nomination> getForDateRange(@PathVariable("from") Date from, 
			@PathVariable("to") Date to,
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "30") Integer pageSize ) {
		return nominationService.getForDateRange(from, to , pageNumber, pageSize);
	}
	

	
	@GetMapping(path="/period/week")
	public @ResponseBody Page<Nomination> getForWeek(@RequestParam(required = false) Integer weekNum, 
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "30") Integer pageSize ) {
		return nominationService.getForWeek(weekNum , pageNumber, pageSize);
	}

}
