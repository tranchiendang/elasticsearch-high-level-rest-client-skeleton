package io.dtc.essearch.service;

import java.io.IOException;
import java.util.Optional;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * This interface is used to provide all method regarding CRUD es index
 * @param <T> This describe type parameter
 */
public interface IndexService<T> {
    /**
     * This method is used to create new es index.
     * @param numShard number of shard.
     * @param numReplica number of replica.
     * @return boolean true / false if successfully created.
     * @throws IOException
     */
    boolean create(Optional<Integer> numShard, Optional<Integer> numReplica) throws IOException;
    /**
     * TODO
     * Implement later
     * @return
     */
    boolean update();
    /**
     * TODO
     * Implement later
     * @return
     */
    boolean delete();
    /**
     * This method is used to build properties for es index
     * @param createIndexRequest object index request (built-in)
     * @param builder object builder contain properties definition.
     * @return CreateIndexRequest ready to use.
     * @throws IOException
     */
    CreateIndexRequest mapping(CreateIndexRequest createIndexRequest, XContentBuilder builder) throws IOException;
}