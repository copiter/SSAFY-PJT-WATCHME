package com.A108.Watchme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WatchmeApplication {

	public static void main(String[] args) {

		SpringApplication.run(WatchmeApplication.class, args);
	}



}
