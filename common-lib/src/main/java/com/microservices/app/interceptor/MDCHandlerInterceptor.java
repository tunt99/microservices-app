package com.microservices.app.interceptor;

import com.microservices.app.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MDCHandlerInterceptor implements HandlerInterceptor {

    private final IdGenerator idGenerator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String correlationId = request.getHeader(Constants.CORRELATION_ID_HEADER);
        if (correlationId != null) {
            MDC.put(Constants.CORRELATION_ID_KEY, correlationId);
        } else {
            MDC.put(Constants.CORRELATION_ID_KEY, idGenerator.generateId().toString());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(Constants.CORRELATION_ID_KEY);
    }
}
