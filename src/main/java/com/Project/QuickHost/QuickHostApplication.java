package com.Project.QuickHost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuickHostApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickHostApplication.class, args);
	}

}
