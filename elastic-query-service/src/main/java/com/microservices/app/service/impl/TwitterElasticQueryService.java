package com.microservices.app.service.impl;

import com.micorservices.app.model.index.impl.TwitterIndexModel;
import com.microservices.app.service.RestServiceCommon;
import com.microservices.app.config.ElasticQueryServiceConfigData;
import com.microservices.app.constant.QueryType;
import com.microservices.app.model.QueryAnalyticsResponseModel;
import com.microservices.app.model.ElasticQueryResponseModel;
import com.microservices.app.model.QueryWordCountResponseModel;
import com.microservices.app.service.ElasticQueryClient;
import com.microservices.app.service.ElasticQueryService;
import com.microservices.app.transformer.ElasticToResponseModelTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterElasticQueryService implements ElasticQueryService {

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;
    private final RestServiceCommon restServiceCommon;

    @Override
    public ElasticQueryResponseModel getDocumentById(String id) {
        log.info("Querying elasticsearch by id {}", id);
        return elasticToResponseModelTransformer.getResponseModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public QueryAnalyticsResponseModel getDocumentByText(String text, String accessToken) {
        log.info("Querying elasticsearch by text {}", text);
        List<ElasticQueryResponseModel> elasticQueryResponseModels =
                elasticToResponseModelTransformer.getResponseModels(elasticQueryClient.getIndexModelByText(text));
        return QueryAnalyticsResponseModel.builder()
                .wordCount(getWordCount(text, accessToken))
                .data(elasticQueryResponseModels)
                .build();
    }

    @Override
    public List<ElasticQueryResponseModel> getAllDocuments() {
        log.info("Querying all documents in elasticsearch");
        return elasticToResponseModelTransformer.getResponseModels(elasticQueryClient.getAllIndexModels());
    }

    private Long getWordCount(String text, String accessToken) {
        if (QueryType.KAFKA_STATE_STORE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            return getFromKafkaStateStore(text, accessToken).getWordCount();

        } else if (QueryType.ANALYTICS_DATABASE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            return getFromAnalyticsDatabase(text, accessToken).getWordCount();
        }
        return 0L;
    }

    private QueryWordCountResponseModel getFromAnalyticsDatabase(String text, String accessToken) {
        ElasticQueryServiceConfigData.Query queryFromAnalyticsDatabase =
                elasticQueryServiceConfigData.getQueryFromAnalyticsDatabase();
        return retrieveResponseModel(text, accessToken, queryFromAnalyticsDatabase);
    }

    private QueryWordCountResponseModel getFromKafkaStateStore(String text, String accessToken) {
        ElasticQueryServiceConfigData.Query queryFromKafkaStateStore =
                elasticQueryServiceConfigData.getQueryFromKafkaStateStore();
        return retrieveResponseModel(text, accessToken, queryFromKafkaStateStore);
    }

    private QueryWordCountResponseModel retrieveResponseModel(String text,
                                                              String accessToken,
                                                              ElasticQueryServiceConfigData.Query query) {

          return restServiceCommon.invokeApi(accessToken, query.getUri(), HttpMethod.GET, null, null,
                QueryWordCountResponseModel.class, text);

    }
}
