package io.dtc.essearch.service.impl;

import java.io.IOException;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.stereotype.Service;
import io.dtc.essearch.configuration.ApplicationConfig;
import io.dtc.essearch.model.Product;
import io.dtc.essearch.service.IndexServiceAbstract;

@Service
public class IndexServiceProductImpl extends IndexServiceAbstract<Product> {

    public IndexServiceProductImpl(RestHighLevelClient restHighLevelClient, ApplicationConfig applicationConfig) {
        this.restHighLevelClient = restHighLevelClient;
        this.applicationConfig = applicationConfig;
        this.indexName = Product.getIndexName(this.applicationConfig.getElasticsearchIndexPrefix());
    }

    @Override
    public XContentBuilder buildBuilder() throws IOException {
        return Product.buildBuilder();
    }
    
}