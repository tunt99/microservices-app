package com.microservices.app.transformer;

import com.micorservices.app.model.index.impl.TwitterIndexModel;
import com.microservices.app.common.config.UtilConfig;
import com.microservices.app.kafka.avro.model.TwitterCustomModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvroToElasticModelTransformer {

    private final UtilConfig utilConfig;

    public List<TwitterIndexModel> getElasticModels(List<String> avroListStr) {

        try {

            List<TwitterCustomModel> avroModels = new ArrayList<>();
            for (String avroStr : avroListStr) {
                TwitterCustomModel item = utilConfig.objectMapper().readValue(avroStr, TwitterCustomModel.class);
                avroModels.add(item);
            }

            return avroModels.stream()
                    .map(avroModel -> TwitterIndexModel
                            .builder()
                            .userId(avroModel.getUserId())
                            .id(String.valueOf(avroModel.getId()))
                            .text(avroModel.getText())
                            .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt()),
                                    ZoneId.systemDefault()))
                            .build()
                    ).collect(Collectors.toList());
        } catch (Exception ex){
            log.info("Error convert models: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }
}
