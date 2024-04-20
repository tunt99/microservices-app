package com.microservices.app.transformer;

import com.microservices.app.config.UtilConfig;
import com.microservices.app.entity.AnalyticsEntity;
import com.microservices.app.kafka.avro.model.TwitterAnalyticsAvroModel;
import com.microservices.app.kafka.avro.model.TwitterAnalyticsCustomModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvroToDbEntityModelTransformer {

    private final IdGenerator idGenerator;
    private final UtilConfig utilConfig;

    public List<AnalyticsEntity> getEntityModel(List<TwitterAnalyticsAvroModel> avroModels) {
        return avroModels.stream().map(avroModel -> new AnalyticsEntity(
                idGenerator.generateId(),
                avroModel.getWord(),
                avroModel.getWordCount(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt()), ZoneOffset.UTC)))
                .collect(toList());
    }


    public List<AnalyticsEntity> mapEntity(List<String> messages) {

        try {


            List<TwitterAnalyticsCustomModel> customModels = new ArrayList<>();
            for (String message : messages) {
                TwitterAnalyticsCustomModel item = utilConfig.objectMapper().readValue(message, TwitterAnalyticsCustomModel.class);
                customModels.add(item);
            }

            return customModels.stream()
                    .map(item -> AnalyticsEntity
                            .builder()
                            .id(UUID.randomUUID())
                            .word(item.getWord())
                            .wordCount(item.getWordCount())
                            .recordDate(LocalDateTime.now())
                            .build()
                    ).collect(Collectors.toList());
        } catch (Exception ex) {
            log.info("Error convert models: {}", ex.getMessage());
            return Collections.emptyList();
        }

    }
}
