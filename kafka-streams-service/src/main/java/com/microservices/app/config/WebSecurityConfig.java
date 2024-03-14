package com.microservices.app.config;

import com.microservices.app.security.KafkaStreamsUserDetailsService;
import com.microservices.app.security.KafkaStreamsUserJwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final KafkaStreamsUserDetailsService kafkaStreamsUserDetailsService;

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Value("${security.paths-to-ignore}")
    private String[] pathsToIgnore;

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> kafkaStreamsUserJwtAuthConverter() {
        return new KafkaStreamsUserJwtConverter(kafkaStreamsUserDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .anyRequest().fullyAuthenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) ->
                        oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(kafkaStreamsUserJwtAuthConverter())));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(pathsToIgnore);
    }

    @Bean
    JwtDecoder jwtDecoder(@Qualifier("kafka-streams-service-audience-validator")
                                  OAuth2TokenValidator<Jwt> audienceValidator) {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(
                oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer(
                        oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withAudience =
                new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }

}