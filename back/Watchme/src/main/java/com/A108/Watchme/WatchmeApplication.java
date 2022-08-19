package com.A108.Watchme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

@EnableScheduling
@SpringBootApplication
public class WatchmeApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.properties,"
			+ "classpath:aws.yml,"
			+ "classpath:oauth.yml";
////	@Bean
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

		new SpringApplicationBuilder(WatchmeApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
