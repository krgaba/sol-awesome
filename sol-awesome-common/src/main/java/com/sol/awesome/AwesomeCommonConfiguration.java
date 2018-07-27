package com.sol.awesome;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan("com.sol.awesome")
@PropertySource(value = "classpath:sol-awesome-common.properties", ignoreResourceNotFound = true)
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@EnableSwagger2
public class AwesomeCommonConfiguration {

}
