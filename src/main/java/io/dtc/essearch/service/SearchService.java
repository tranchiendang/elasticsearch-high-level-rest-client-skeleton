package io.dtc.essearch.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SearchInterface provide all method regarding search, suggestion.
 * @param <T> This describe type parameter
 */
public interface SearchService<T> {
    /**
     * This methods is used to search with filter, range, and paging
     * @param criterial used as primary key for searching
     * @param filterMap filter will be apply for searching
     * @param priceMap range will be apply for searching
     * @param offset the page wanted
     * @param limit the limit per page
     * @return List of type parameter
     * @throws IOException
     */
    List<T> doSearchWithFilter(String criterial, Map<String, String> filterMap, Map<String, String> priceMap, int offset, int limit) throws IOException;
}