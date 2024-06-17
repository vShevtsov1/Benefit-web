package com.example.Redi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class RediApplication {

	public static void main(String[] args) {
		SpringApplication.run(RediApplication.class, args);
	}

}
