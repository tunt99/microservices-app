package com.microservices.app.service;


import com.microservices.app.model.ElasticQueryWebClientRequestModel;
import com.microservices.app.model.ElasticQueryWebClientResponseModel;

import java.util.List;

public interface ElasticQueryWebClient {

    List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel);
}
