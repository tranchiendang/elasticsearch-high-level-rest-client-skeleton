package io.dtc.essearch.service;

import java.io.IOException;
import java.util.Optional;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import io.dtc.essearch.configuration.ApplicationConfig;

/**
 * This abstract class is a base class for all index with type parameter
 */
public abstract class IndexServiceAbstract<T> implements IndexService<T> {
    
    public abstract XContentBuilder buildBuilder() throws IOException;

    protected RestHighLevelClient restHighLevelClient;
    
    protected ApplicationConfig applicationConfig;

    protected String indexName;

    @Override
    public boolean create(Optional<Integer> numShardOpt, Optional<Integer> numReplicaOpt) throws IOException {
        int numShard = numShardOpt.orElse(3);
        int numReplica = numReplicaOpt.orElse(2);

        Settings.Builder settings = Settings.builder().put("index.number_of_shards", numShard).put("index.number_of_replicas", numReplica)
            .loadFromSource(Strings.toString(
                XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject("analysis")
                            .startObject("analyzer")
                                .startObject("underscore_analyzer")
                                    .field("tokenizer", "underscore_tokenizer")
                                .endObject()
                                .startObject("hyphen_analyzer")
                                    .field("tokenizer", "hyphen_tokenizer")
                                .endObject()
                            .endObject()
                            .startObject("tokenizer")
                                .startObject("underscore_tokenizer")
                                    .field("type", "simple_pattern_split")
                                    .field("pattern", "_")
                                .endObject()
                                .startObject("hyphen_tokenizer")
                                    .field("type", "simple_pattern_split")
                                    .field("pattern", "-")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()), 
                XContentType.JSON);

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(this.indexName);
        createIndexRequest.settings(settings);
        
        this.mapping(createIndexRequest, this.buildBuilder());
        CreateIndexResponse createIndexResponse = this.restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged(); 
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged(); 
        return acknowledged & shardsAcknowledged;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public CreateIndexRequest mapping(CreateIndexRequest createIndexRequest, XContentBuilder builder) throws IOException {
        createIndexRequest.mapping(builder);
        return createIndexRequest;
    }
}