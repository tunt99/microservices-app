package com.microservices.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryAnalyticsResponseModel {
    private Long wordCount;
    private List<ElasticQueryResponseModel> data;
}
