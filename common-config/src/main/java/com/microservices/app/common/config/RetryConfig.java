package com.microservices.app.common.config;

import com.microservices.app.config.RetryConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RetryConfig {

    private final RetryConfigData retryConfigData;

    private static final long DEFAULT_INITIAL_INTERVAL_MS = 1000;
    private static final long DEFAULT_MAX_INTERVAL_MS = 10000;
    private static final double DEFAULT_MULTIPLIER = 2.0;
    private static final int DEFAULT_MAX_ATTEMPTS = 3;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        long initialIntervalMs = retryConfigData.getInitialIntervalMs() != null ?
                retryConfigData.getInitialIntervalMs() : DEFAULT_INITIAL_INTERVAL_MS;
        long maxIntervalMs = retryConfigData.getMaxIntervalMs() != null ?
                retryConfigData.getMaxIntervalMs() : DEFAULT_MAX_INTERVAL_MS;
        double multiplier = retryConfigData.getMultiplier() != null ?
                retryConfigData.getMultiplier() : DEFAULT_MULTIPLIER;
        int maxAttempts = retryConfigData.getMaxAttempts() != null ?
                retryConfigData.getMaxAttempts() : DEFAULT_MAX_ATTEMPTS;

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(initialIntervalMs);
        exponentialBackOffPolicy.setMaxInterval(maxIntervalMs);
        exponentialBackOffPolicy.setMultiplier(multiplier);

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(maxAttempts);

        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }
}



