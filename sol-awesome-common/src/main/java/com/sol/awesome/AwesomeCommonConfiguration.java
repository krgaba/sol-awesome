package com.sol.awesome;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.sol.awesome")
@PropertySource(value = "classpath:sol-awesome-common.properties", ignoreResourceNotFound = true)
@EnableAspectJAutoProxy
public class AwesomeCommonConfiguration {

}
