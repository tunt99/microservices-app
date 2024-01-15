package com.microservices.app.service;

public interface KafkaProducer {
    void send(String topicName, String key, Object message);
}
