package com.microservices.app.service.impl;

import com.microservices.app.model.AnalyticsResponseModel;
import com.microservices.app.repository.AnalyticsRepository;
import com.microservices.app.service.AnalyticsService;
import com.microservices.app.transformer.EntityToResponseModelTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwitterAnalyticsService implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    private final EntityToResponseModelTransformer entityToResponseModelTransformer;

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics(String word) {
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.getAnalyticsEntitiesByWord(word, PageRequest.of(0, 1))
                        .stream().findFirst().orElse(null));
    }
}
