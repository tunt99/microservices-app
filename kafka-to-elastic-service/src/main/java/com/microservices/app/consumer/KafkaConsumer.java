package com.microservices.app.consumer;

import java.util.List;

public interface KafkaConsumer {
    void receive(List<String> message, List<Integer> keys, List<Integer> partitions, List<Long> offsets);
}
