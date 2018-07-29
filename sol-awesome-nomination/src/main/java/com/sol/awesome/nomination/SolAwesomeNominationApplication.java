package com.sol.awesome.nomination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.sol.awesome"})
@EnableFeignClients
public class SolAwesomeNominationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolAwesomeNominationApplication.class, args);
	}
}
