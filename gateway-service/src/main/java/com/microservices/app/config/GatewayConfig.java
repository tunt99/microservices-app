package com.microservices.app.config;

import com.microservices.app.constant.Constants;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final GatewayServiceConfigData data;

    /**
     * Use for the rate limiter
     */
    @Bean(name = "authHeaderResolver")
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange
                .getRequest().getHeaders().getFirst(Constants.HEADER_AUTHORIZATION)));
    }

    @Bean
    Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
        return reactiveResilience4JCircuitBreakerFactory ->
                reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofMillis(data.getTimeoutMs()))
                                .build())
                        .circuitBreakerConfig(CircuitBreakerConfig.custom()
                                .failureRateThreshold(data.getFailureRateThreshold())
                                .slowCallRateThreshold(data.getSlowCallRateThreshold())
                                .slowCallDurationThreshold(Duration.ofMillis(data.getSlowCallDurationThreshold()))
                                .permittedNumberOfCallsInHalfOpenState(data.getPermittedNumOfCallsInHalfOpenState())
                                .slidingWindowSize(data.getSlidingWindowSize())
                                .minimumNumberOfCalls(data.getMinNumberOfCalls())
                                .waitDurationInOpenState(Duration.ofMillis(data.getWaitDurationInOpenState()))
                                .build())
                        .build());
    }
}
