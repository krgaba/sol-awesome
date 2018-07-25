package com.sol.awesome.nomination.client;

import java.util.Set;

import org.springframework.stereotype.Component;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sol.awesome.nomination.domain.AwesomeEmployee;

//@FeignClient(name = "employee", url = "http://localhost:8080/employees")
@Component
public class AwesomeEmployeeClient {
	/*
	@GetMapping(path = "/search/findByIdIn")
	Resource<AwesomeEmployee> getEmployees(@RequestParam("ids") Set<Long> ids);
*/
}
