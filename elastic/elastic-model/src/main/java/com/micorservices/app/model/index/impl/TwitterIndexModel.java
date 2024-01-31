package com.micorservices.app.model.index.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.micorservices.app.model.index.IndexModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Builder
@Document(indexName = "#{@elasticConfigData.indexName}")
public class TwitterIndexModel implements IndexModel {

    @JsonProperty
    private String id;

    @JsonProperty
    private Long userId;

    @JsonProperty
    private String text;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty
    private LocalDateTime createdAt;

}
