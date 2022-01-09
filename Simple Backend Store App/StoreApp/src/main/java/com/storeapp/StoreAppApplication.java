package com.storeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import web.HomeController;
import web.WebCalculationService;
@EnableAutoConfiguration
@EnableEurekaClient
@ComponentScan(useDefaultFilters = false)
@EnableCaching
@SpringBootApplication
public class StoreAppApplication {

	public static final String CALCULATION_SERVICE_URL = "http://calculation-service";

	public static void main(String[] args) {
		SpringApplication.run(StoreAppApplication.class, args);
	}

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public WebCalculationService calculationService() {
		return new WebCalculationService(CALCULATION_SERVICE_URL);
	}

	@Bean
	public HomeController homeController() {
		return new HomeController();
	}
	
}
