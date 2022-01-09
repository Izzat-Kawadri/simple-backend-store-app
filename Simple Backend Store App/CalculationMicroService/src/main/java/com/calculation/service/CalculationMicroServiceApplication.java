package com.calculation.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
//@EnableDiscoveryClient
@EnableEurekaClient
@ComponentScan
@SpringBootApplication
public class CalculationMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculationMicroServiceApplication.class, args);
	}

}
