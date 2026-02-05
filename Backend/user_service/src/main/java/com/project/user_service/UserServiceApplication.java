package com.project.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {

	public static void main(String[] args) {

<<<<<<< HEAD
		System.out.println("Hello from TestOut");
=======
		System.out.println("Hello from TestOut1");
>>>>>>> 7000dbe5da5246742079a2e3318e50c5703ef107
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
