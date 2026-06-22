package com.solmed.solmedbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolmedbackendApplication {
// run in powershell with .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local" 
// because of the application-local
	public static void main(String[] args) {
		SpringApplication.run(SolmedbackendApplication.class, args);
	}

}
