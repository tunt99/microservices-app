package com.microservices.app.common.util;

import com.microservices.app.common.config.UtilConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FunctionsUtil {

    private final UtilConfig utilConfig;

    public String parseString(Object object) {
        try {
            return utilConfig.objectMapper().writeValueAsString(object);
        } catch (Exception ex) {
            log.error("Parse to string ex {}", ex.getMessage());
            return null;
        }
    }
}
