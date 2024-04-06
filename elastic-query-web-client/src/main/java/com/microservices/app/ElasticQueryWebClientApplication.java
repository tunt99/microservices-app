package com.microservices.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.microservices.app")
public class ElasticQueryWebClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryWebClientApplication.class, args);
    }
}
