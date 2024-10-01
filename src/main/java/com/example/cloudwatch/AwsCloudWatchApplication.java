package com.example.cloudwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AwsCloudWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsCloudWatchApplication.class, args);
		System.setProperty("com.amazonaws.sdk.disabledEc2Metadata", "true");
	}

}
