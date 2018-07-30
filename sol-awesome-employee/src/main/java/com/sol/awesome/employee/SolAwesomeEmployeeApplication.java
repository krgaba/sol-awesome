package com.sol.awesome.employee;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = {"com.sol.awesome"})
public class SolAwesomeEmployeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolAwesomeEmployeeApplication.class, args);
    }
}
