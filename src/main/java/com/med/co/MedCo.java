package com.med.co;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedCo {

	public static void main(String[] args) {
		SpringApplication.run(MedCo.class, args);
	
	}

}