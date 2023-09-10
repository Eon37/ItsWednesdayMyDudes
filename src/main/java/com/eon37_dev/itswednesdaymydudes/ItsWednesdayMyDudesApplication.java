package com.eon37_dev.itswednesdaymydudes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ItsWednesdayMyDudesApplication {
	public static void main(String[] args) {
		SpringApplication.run(ItsWednesdayMyDudesApplication.class, args);
	}

}
