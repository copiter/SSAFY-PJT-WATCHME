package com.A108.Watchme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

@EnableScheduling
@SpringBootApplication
public class WatchmeApplication {
//	@Bean
//	public S3Client client() {
//		return S3Client.create();
//	}

//	@Bean
//	public S3Client client() {
//		return S3Client.builder()
//				.credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
//				.build();
//	}

	public static void main(String[] args) {

		SpringApplication.run(WatchmeApplication.class, args);
	}



}
