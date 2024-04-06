package com.microservices.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.microservices.app", exclude = {KafkaAutoConfiguration.class})
public class KafkaStreamsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsServiceApplication.class, args);
	}

}