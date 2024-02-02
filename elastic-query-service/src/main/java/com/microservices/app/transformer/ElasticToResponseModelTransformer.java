package com.microservices.app.transformer;

import com.micorservices.app.model.index.impl.TwitterIndexModel;
import com.microservices.app.common.config.UtilConfig;
import com.microservices.app.model.ElasticQueryServiceResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ElasticToResponseModelTransformer {

    private final UtilConfig utilConfig;

    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel twitterIndexModel) {
        return utilConfig.objectMapper().convertValue(twitterIndexModel, ElasticQueryServiceResponseModel.class);
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
