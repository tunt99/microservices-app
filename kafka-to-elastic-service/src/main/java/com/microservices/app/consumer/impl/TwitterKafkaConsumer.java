package com.microservices.app.consumer.impl;

import com.microservices.app.config.KafkaConfigData;
import com.microservices.app.consumer.KafkaConsumer;
import com.microservices.app.kafka.admin.client.KafkaAdminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterKafkaConsumer implements KafkaConsumer {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        MessageListenerContainer messageListenerContainer =
                kafkaListenerEndpointRegistry.getListenerContainer("twitterTopicListener");
        if (messageListenerContainer != null){
            messageListenerContainer.start();
        }
    }

    @Override
    @KafkaListener(id = "twitterTopicListener", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<String> message) {
        log.info("KAFKA CONSUME: {}", message);
    }
}
