package com.microservices.app.runner.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservices.app.common.config.UtilConfig;
import com.microservices.app.config.KafkaStreamsConfigData;
import com.microservices.app.kafka.avro.model.TwitterAnalyticsCustomModel;
import com.microservices.app.runner.StreamsRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;


@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaStreamsRunner implements StreamsRunner<String, Long> {

    private static final String REGEX = "\\W+";
    private static final Serde<String> STRING_SERDE = Serdes.String();

    private final KafkaStreamsConfigData kafkaStreamsConfigData;
    private final StreamsBuilderFactoryBean factoryBean;
    private final UtilConfig utilConfig;

    @Autowired
    private void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
                .stream(kafkaStreamsConfigData.getInputTopicName(), Consumed.with(STRING_SERDE, STRING_SERDE));

        KTable<String, Long> wordCounts = messageStream
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(value -> Arrays.asList(value.split(REGEX)))
                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
                .count(Materialized
                        .<String, Long, KeyValueStore<Bytes, byte[]>>as(kafkaStreamsConfigData.getWordCountStoreName()));
        wordCounts.toStream()
                .map(mapToAnalyticsModel())
                .to(kafkaStreamsConfigData.getOutputTopicName());
    }

    private KeyValueMapper<String, Long, KeyValue<? extends String, ? extends String>>
    mapToAnalyticsModel() {
        return (word, count) -> {
            log.info("Sending to topic {}, word {} - count {}",
                    kafkaStreamsConfigData.getOutputTopicName(), word, count);
            try {
                return new KeyValue<>(word, utilConfig.objectMapper().writeValueAsString(TwitterAnalyticsCustomModel
                        .builder()
                        .word(word)
                        .wordCount(count)
                        .createdAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                        .build()));
            } catch (JsonProcessingException e) {
                log.info("Error mapper {}", e.getMessage());
                return new KeyValue<>("", "");
            }
        };
    }

    @Override
    public Long getValueByKey(String word) {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(kafkaStreamsConfigData.getWordCountStoreName(), QueryableStoreTypes.keyValueStore())
        );
        return counts.get(word);
    }
}
