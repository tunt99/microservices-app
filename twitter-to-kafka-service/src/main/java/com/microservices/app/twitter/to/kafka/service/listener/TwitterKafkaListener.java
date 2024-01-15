package com.microservices.app.twitter.to.kafka.service.listener;

import com.microservices.app.common.config.UtilConfig;
import com.microservices.app.config.KafkaConfigData;
import com.microservices.app.kafka.avro.model.TwitterCustomModel;
import com.microservices.app.service.KafkaProducer;
import com.microservices.app.twitter.to.kafka.service.transformer.TwitterStatusToAvroTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterKafkaListener extends StatusAdapter {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer kafkaProducer;
    private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;
    private final UtilConfig utilConfig;

    @Override
    public void onStatus(Status status) {
        log.info("Received status text {} sending to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterCustomModel twitterAvroModel = twitterStatusToAvroTransformer.getTwitterCustomModelFromStatus(status);

        kafkaProducer.send(kafkaConfigData.getTopicName(), String.valueOf(twitterAvroModel), utilConfig.parseString(twitterAvroModel));
    }
}
