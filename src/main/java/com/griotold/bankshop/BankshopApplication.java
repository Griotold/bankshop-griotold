package com.griotold.bankshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BankshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankshopApplication.class, args);
	}

}
