package com.microservices.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueryType {

    KAFKA_STATE_STORE("KAFKA_STATE_STORE"), ANALYTICS_DATABASE("ANALYTICS_DATABASE");

    private final String type;
}
