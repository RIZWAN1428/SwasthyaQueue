package com.swasthyaqueue.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //It tells spring scan for @scheduled method and run them.
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		System.setProperty("user.timezone", "Asia/Kolkata");
		SpringApplication.run(BackendApplication.class, args);
	}

}
