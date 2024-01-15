package com.microservices.app.kafka.avro.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwitterCustomModel {

    private Long userId;
    private Long id;
    private String text;
    private Long createdAt;
}
