package com.microservices.app.config;

import com.microservices.app.interceptor.CustomClientHttpRequestInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "appRestTemplate")
    @LoadBalanced
    public RestTemplate appRestTemplate() {

        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(5000);

        RestTemplate  restClient = new RestTemplate(
                new BufferingClientHttpRequestFactory(clientHttpRequestFactory));

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new CustomClientHttpRequestInterceptor());
        restClient.setInterceptors(interceptors);

        return restClient;
    }
}
