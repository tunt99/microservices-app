package com.microservices.app.twitter.to.kafka.service.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestConsume {

    /*@KafkaListener(containerFactory = "kafkaListenerContainerFactory",
            topics = "${kafka-config.topic-name}",
            groupId = "${kafka-config.group-id}")*/
    public void testConsume(String message){
        log.info("Receive {}", message);
    }
}
