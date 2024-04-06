package com.microservices.app.transformer;

import com.micorservices.app.model.index.impl.TwitterIndexModel;
import com.microservices.app.common.config.UtilConfig;
import com.microservices.app.model.ElasticQueryResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ElasticToResponseModelTransformer {

    private final UtilConfig utilConfig;

    public ElasticQueryResponseModel getResponseModel(TwitterIndexModel twitterIndexModel) {
        return utilConfig.objectMapper().convertValue(twitterIndexModel, ElasticQueryResponseModel.class);
    }

    public List<ElasticQueryResponseModel> getResponseModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
