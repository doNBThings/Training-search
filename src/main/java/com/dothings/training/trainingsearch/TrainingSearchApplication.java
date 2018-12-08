package com.dothings.training.trainingsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TrainingSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingSearchApplication.class, args);
	}
}
