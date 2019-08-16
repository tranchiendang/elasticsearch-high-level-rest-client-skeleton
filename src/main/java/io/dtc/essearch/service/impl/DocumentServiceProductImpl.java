package io.dtc.essearch.service.impl;

import java.io.IOException;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;
import io.dtc.essearch.configuration.ApplicationConfig;
import io.dtc.essearch.model.Product;
import io.dtc.essearch.service.DocumentServiceAbstract;

@Service
public class DocumentServiceProductImpl extends DocumentServiceAbstract<Product> {

    public DocumentServiceProductImpl(RestHighLevelClient restHighLevelClient, ApplicationConfig applicationConfig) {
        this.restHighLevelClient = restHighLevelClient;
        this.applicationConfig = applicationConfig;
        this.indexName = Product.getIndexName(this.applicationConfig.getElasticsearchIndexPrefix());
    }

    @Override
    public long indexDocument(Product obj) throws IOException {
        long id = this.indexDoc(obj, obj.getId());
        return id;
    }

}