package com.codes.study_core_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StudyCoreServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyCoreServiceApplication.class, args);
	}

}
