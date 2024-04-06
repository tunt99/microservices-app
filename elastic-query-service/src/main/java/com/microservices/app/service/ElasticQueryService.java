package com.microservices.app.service;


import com.microservices.app.model.ElasticQueryResponseModel;
import com.microservices.app.model.QueryAnalyticsResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryResponseModel getDocumentById(String id);

    QueryAnalyticsResponseModel getDocumentByText(String text, String accessToken);

    List<ElasticQueryResponseModel> getAllDocuments();
}
