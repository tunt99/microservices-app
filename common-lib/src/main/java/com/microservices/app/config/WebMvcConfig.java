package com.microservices.app.config;

import com.microservices.app.interceptor.MDCHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MDCHandlerInterceptor mdcHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcHandlerInterceptor);
    }
}
