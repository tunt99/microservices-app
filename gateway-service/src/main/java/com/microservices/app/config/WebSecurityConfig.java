package com.microservices.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain webFluxSecurityConfig(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .anyExchange().permitAll()
                );
        return httpSecurity.build();
    }
}
