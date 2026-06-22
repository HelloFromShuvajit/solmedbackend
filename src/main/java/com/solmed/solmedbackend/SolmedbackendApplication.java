package com.solmed.solmedbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolmedbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolmedbackendApplication.class, args);
	}

}
