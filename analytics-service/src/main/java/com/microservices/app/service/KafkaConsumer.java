package com.microservices.app.service;

import java.util.List;

public interface KafkaConsumer {
    void receive(List<String> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
