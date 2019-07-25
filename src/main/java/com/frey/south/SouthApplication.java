package com.frey.south;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SouthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SouthApplication.class, args);
	}

}
