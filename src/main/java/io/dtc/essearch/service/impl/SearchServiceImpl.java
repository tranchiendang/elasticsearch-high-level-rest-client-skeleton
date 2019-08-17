package io.dtc.essearch.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import io.dtc.essearch.configuration.ApplicationConfig;
import io.dtc.essearch.model.Product;
import io.dtc.essearch.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService<Product> {

    private RestHighLevelClient restHighLevelClient;

    private ApplicationConfig applicationConfig;

    public SearchServiceImpl(ApplicationConfig applicationConfig, RestHighLevelClient restHighLevelClient) {
        this.applicationConfig = applicationConfig;
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public List<Product> doSearch(String criterial, Map<String, String> filterMap, Map<String, String> priceMap, int offset, int limit) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder queryBuilder = this.getSearchSourceBuilder(criterial);
        queryBuilder = this.applyFilter(queryBuilder, filterMap);
        queryBuilder = this.applyPricingRange(queryBuilder, priceMap);
        
        searchSourceBuilder.query(queryBuilder).from(offset*limit).size(limit);

        String indexName = Product.getIndexName(this.applicationConfig.getElasticsearchIndexPrefix());
        SearchRequest searchRequest = new SearchRequest(indexName);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        List<Product> resultList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : searchHits) {
            Product product = mapper.readValue(hit.getSourceAsString(), Product.class);
            resultList.add(product);
        }

        return resultList;
    }

    private BoolQueryBuilder getSearchSourceBuilder(String criterial) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder multimatchQueryBuilder = new MultiMatchQueryBuilder(criterial, Product.getListFieldForSearch().toArray(new String[0]));
        
        boolQueryBuilder.must(multimatchQueryBuilder);
        return boolQueryBuilder;
    }

    private BoolQueryBuilder applyFilter(BoolQueryBuilder boolQueryBuilder, Map<String, String> filterMap) {
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            TermQueryBuilder tqb = QueryBuilders.termQuery(entry.getKey(), Integer.parseInt(entry.getValue()));
            boolQueryBuilder.filter(tqb);
        }

        return boolQueryBuilder;
    }

    private BoolQueryBuilder applyPricingRange(BoolQueryBuilder boolQueryBuilder, Map<String, String> priceMap) {
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("price");
        if (priceMap.getOrDefault("from", null) != null) {
            queryBuilder.from(Double.parseDouble(priceMap.get("from")));
        }
        if (priceMap.getOrDefault("to", null) != null) {
            queryBuilder.to(Double.parseDouble(priceMap.get("to")));
        }
        boolQueryBuilder.must(queryBuilder);
        return boolQueryBuilder;
    }

    

}