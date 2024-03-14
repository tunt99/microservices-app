package com.microservices.app.kafka.avro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwitterAnalyticsCustomModel {

    private String word;
    private Long wordCount;
    private Long createdAt;
}
