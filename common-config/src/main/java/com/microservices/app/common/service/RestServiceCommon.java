package com.microservices.app.common.service;

import org.springframework.http.HttpMethod;

import java.util.Map;

public interface RestServiceCommon {
    <T> T invokeApi(String authorization, String url, HttpMethod method, Map<String, Object> params,
                    Object body, Class<T> targetResponse, Object... uriVariableValues);
}
