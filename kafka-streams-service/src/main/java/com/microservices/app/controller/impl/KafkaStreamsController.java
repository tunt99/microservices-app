package com.microservices.app.controller.impl;

import com.microservices.app.controller.KafkaStreamApi;
import com.microservices.app.model.KafkaStreamsResponseModel;
import com.microservices.app.runner.StreamsRunner;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KafkaStreamsController implements KafkaStreamApi {

    private final StreamsRunner<String, Long> kafkaStreamsRunner;

    public ResponseEntity<KafkaStreamsResponseModel> getWordCountByWord(@PathVariable @NotEmpty String word) {

        Long wordCount = kafkaStreamsRunner.getValueByKey(word.toLowerCase(Locale.ROOT));
        log.info("Word count {} returned for word {}", wordCount, word);
        return ResponseEntity.ok(KafkaStreamsResponseModel.builder()
                .word(word)
                .wordCount(wordCount)
                .build());
    }
}
