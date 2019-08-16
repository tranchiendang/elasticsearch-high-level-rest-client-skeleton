package io.dtc.essearch.service;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import lombok.extern.slf4j.Slf4j;
import io.dtc.essearch.configuration.ApplicationConfig;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;

/**
 * This abstract class act as base class for doing index to es
 * @param <T>
 */
@Slf4j
public abstract class DocumentServiceAbstract<T> implements DocumentService<T> {

    protected RestHighLevelClient restHighLevelClient;

    protected ApplicationConfig applicationConfig;
    
    protected String indexName;

    public long indexDoc(T obj, long id) throws IOException {
        IndexRequest indexRequest = new IndexRequest(this.indexName);
        indexRequest.id(String.valueOf(id));
        indexRequest.source(new ObjectMapper().writeValueAsString(obj), XContentType.JSON);
        this.indexRequest(indexRequest, RequestOptions.DEFAULT);
        return id;
    }

    public String indexRequest(IndexRequest indexRequest, RequestOptions options) throws IOException {
        IndexResponse indexResponse = null;
        try {
            indexResponse = this.restHighLevelClient.index(indexRequest, options);
        } catch(ElasticsearchException ex) {
            throw new RuntimeException(ex);
        }

        if(indexResponse != null) {
            String index = indexResponse.getIndex();
            String id = indexResponse.getId();
            long version = indexResponse.getVersion();
            log.info(String.format("%s %s %s", index, id, version));
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                log.info("Document Created.");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                log.info("Document Updated.");
            }
        }

        return indexResponse.getResult().getLowercase();
    }
}