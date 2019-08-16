package io.dtc.essearch.service;

import java.io.IOException;

/***
 * This interface is used to provide all methods regarding doing index document
 * @param <T> This describes type parameter
 */
public interface DocumentService<T> {

    /**
     * This method is used to index any document of type T
     * @param obj type parameter represent es document
     * @return id of indexed document
     * @throws IOException
     */
    long indexDocument(T obj) throws IOException;

}