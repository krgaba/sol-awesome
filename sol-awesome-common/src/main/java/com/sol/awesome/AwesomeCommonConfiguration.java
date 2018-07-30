package com.sol.awesome;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(basePackages = { "com.sol.awesome" })
@PropertySource(value = "classpath:sol-awesome-common.properties", ignoreResourceNotFound = true)
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@EnableSwagger2
//TODO: uncomment when swagger bug is fixed 
@Import(SpringDataRestConfiguration.class)
public class AwesomeCommonConfiguration {

}
