package com.Project.QuickHost;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync //Create create proxy arround async
public class QuickHostApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickHostApplication.class, args);
	}

//	//Performing test
//	@Bean
//	CommandLineRunner llmSmoke(ChatLanguageModel m) {
//		return args -> System.out.println("Gemini says: " + m.generate("am i  asking you to given reply in json format or by default u give this in json formar"));
//	}

//	downloads % ~/Downloads/stripe listen --forward-to localhost:8080/api/v1/webhook/payment
}
