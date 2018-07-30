package com.sol.awesome.nomination.client;

import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sol.awesome.nomination.domain.AwesomeEmployee;
import com.sol.awesome.util.LogInfo;

@LogInfo
@FeignClient("sol-awesome-employee")
public interface AwesomeEmployeeClient {
	
	@GetMapping(path = "/employees/search/findByIdIn")
	Resources<AwesomeEmployee> getEmployees(@RequestParam("ids") Set<Long> ids);

}
