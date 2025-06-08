package com.example.Redi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
		info = @Info(
				title = "Redi API",
				version = "1.0"


		),
		servers = {
				@Server(url = "http://localhost:8086", description = "Local Server"),
				@Server(url = "https://server-benefit.redi.partners/", description = "Production Server")
		}
)
public class RediApplication {

	public static void main(String[] args) {
		SpringApplication.run(RediApplication.class, args);
	}

}
