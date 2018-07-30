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
import com.sol.awesome.util.LogInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Nomination")
@RestController
@RequestMapping("/nominations")
@LogInfo
public class NominationController {
	private final NominationService nominationService;

	public NominationController(NominationService nominationService) {
		this.nominationService = nominationService;
	}

	@ApiOperation("Creates new nomination for the employee")
	@ApiResponses({ @ApiResponse(code = 201, message = "Success") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Nomination create(@RequestBody @Valid Nomination request) {
		return nominationService.create(request);
	}

	@ApiOperation("Finds page of nominations for the employee")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@GetMapping(path = "/employee/{id}")
	public @ResponseBody Page<Nomination> getNominationsForEmployee(@PathVariable("id") Long id,
			@ApiParam(value = "0 based paged number", defaultValue = "0") @RequestParam(defaultValue = "0") Integer pageNumber,
			@ApiParam(value = "page size", defaultValue = "100") @RequestParam(defaultValue = "100") Integer pageSize) {
		return nominationService.getNominationsForEmployee(id, pageNumber, pageSize);
	}

	@ApiOperation("Finds page of nominations for the time period in the date range")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@GetMapping(path = "/period/from/{from}/to/{to}")
	public @ResponseBody Page<Nomination> getForDateRange(@PathVariable("from") Date from, @PathVariable("to") Date to,
			@ApiParam(value = "0 based paged number", defaultValue = "0") @RequestParam(defaultValue = "0") Integer pageNumber,
			@ApiParam(value = "page size", defaultValue = "100") @RequestParam(defaultValue = "100") Integer pageSize) {
		return nominationService.getForDateRange(from, to, pageNumber, pageSize);
	}

	@ApiOperation("Finds page of nominations for the given week of current year if specified, otherwise for the current week")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@GetMapping(path = "/period/week")
	public @ResponseBody Page<Nomination> getForWeek(
			@ApiParam(value = "week number in the year", allowableValues = "range[1-52]", required = false) @RequestParam(required = false) Integer weekNum,
			@ApiParam(value = "0 based paged number", defaultValue = "0") @RequestParam(defaultValue = "0") Integer pageNumber,
			@ApiParam(value = "page size", defaultValue = "100") @RequestParam(defaultValue = "100") Integer pageSize) {
		return nominationService.getForWeek(weekNum, pageNumber, pageSize);
	}

}
