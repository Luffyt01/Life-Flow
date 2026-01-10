package com.life_flow.geolocation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class GeolocationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeolocationServiceApplication.class, args);
	}

}
