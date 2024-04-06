package com.microservices.app.controller.impl;

import com.microservices.app.controller.ElasticDocumentApi;
import com.microservices.app.model.ElasticQueryRequestModel;
import com.microservices.app.model.ElasticQueryResponseModel;
import com.microservices.app.model.QueryAnalyticsResponseModel;
import com.microservices.app.service.ElasticQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
public class ElasticDocumentController implements ElasticDocumentApi {

    private final ElasticQueryService elasticQueryService;

    @Value("${server.port}")
    private String port;

    public ResponseEntity<List<ElasticQueryResponseModel>> getAllDocuments() {
        List<ElasticQueryResponseModel> response = elasticQueryService.getAllDocuments();
        log.info("Elasticsearch returned {} of documents on port {}", response.size(), port);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ElasticQueryResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryResponseModel elasticQueryResponseModel = elasticQueryService.getDocumentById(id);
        log.debug("Elasticsearch returned document with id {} on port {}", id, port);
        return ResponseEntity.ok(elasticQueryResponseModel);
    }

    public ResponseEntity<QueryAnalyticsResponseModel>
    getDocumentByText(@RequestBody @Valid ElasticQueryRequestModel elasticQueryRequestModel,
                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        QueryAnalyticsResponseModel response =
                elasticQueryService.getDocumentByText(elasticQueryRequestModel.getText(),
                        authorization);
        log.info("Elasticsearch returned {} of documents on port {}",
                response.getData().size(), port);
        return ResponseEntity.ok(response);
    }

}
