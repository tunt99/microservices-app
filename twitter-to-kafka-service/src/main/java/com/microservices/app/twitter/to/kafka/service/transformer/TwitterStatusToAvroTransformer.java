package com.microservices.app.twitter.to.kafka.service.transformer;

import com.microservices.app.kafka.avro.model.TwitterAvroModel;
import com.microservices.app.kafka.avro.model.TwitterCustomModel;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class TwitterStatusToAvroTransformer {

    public TwitterAvroModel getTwitterAvroModelFromStatus(Status status) {
        return TwitterAvroModel
                .newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(status.getCreatedAt().getTime())
                .build();
    }

    public TwitterCustomModel getTwitterCustomModelFromStatus(Status status) {
        return TwitterCustomModel
                .builder()
                .id(status.getId())
                .userId(status.getUser().getId())
                .text(status.getText())
                .createdAt(status.getCreatedAt().getTime())
                .build();
    }
}
