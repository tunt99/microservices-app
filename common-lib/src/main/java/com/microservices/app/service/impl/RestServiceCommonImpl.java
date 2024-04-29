package com.microservices.app.service.impl;

import com.microservices.app.constant.Constants;
import com.microservices.app.exception.BaseResponseException;
import com.microservices.app.service.RestServiceCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestServiceCommonImpl implements RestServiceCommon {

    private final RestTemplate appRestTemplate;

    public <T> T invokeApi(String authorization, String url, HttpMethod method, Map<String, Object> params,
                           Object body, Class<T> targetResponse, Object... uriVariableValues) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization);
        httpHeaders.set(Constants.CORRELATION_ID_HEADER, MDC.get(Constants.CORRELATION_ID_KEY));

        HttpEntity<Object> entity = new HttpEntity<>(body, httpHeaders);

        try {
            return appRestTemplate.exchange(url, method, entity, targetResponse, uriVariableValues).getBody();
        } catch (ResourceAccessException ex){
            log.info("Response timeout: {}", ex.getMessage());
            throw new ResourceAccessException(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase());
        } catch (Exception ex){
            log.info("Response error: {}", ex.getMessage());
            throw new BaseResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }
}
