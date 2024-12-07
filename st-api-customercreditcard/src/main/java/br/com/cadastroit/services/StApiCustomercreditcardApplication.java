package br.com.cadastroit.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class StApiCustomercreditcardApplication {

	public static void main(String[] args) {
		SpringApplication.run(StApiCustomercreditcardApplication.class, args);
	}

}
