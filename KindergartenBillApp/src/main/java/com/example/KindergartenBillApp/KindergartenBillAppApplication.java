package com.example.KindergartenBillApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KindergartenBillAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(KindergartenBillAppApplication.class, args);
	}

}
