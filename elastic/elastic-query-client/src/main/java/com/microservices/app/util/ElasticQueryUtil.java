package com.microservices.app.util;

import com.micorservices.app.model.index.IndexModel;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ElasticQueryUtil<T extends IndexModel> {

    public Query getSearchQueryById(String id) {
        return new SearchTemplateQueryBuilder()
                .withIds(Collections.singleton(id))
                .build();
    }

    public Query getSearchQueryByFieldText(String field, String text) {

        Map<String, Object> params = new HashMap<>();
        params.put(field, text);
        return new SearchTemplateQueryBuilder()
                .withParams(params)
                .build();
    }

    public Query getSearchQueryForAll() {
        return new SearchTemplateQueryBuilder()
                .build();
    }
}
