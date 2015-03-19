package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.visearch.internal.DataOperations;
import com.visenze.visearch.internal.DataOperationsImpl;
import com.visenze.visearch.internal.SearchOperations;
import com.visenze.visearch.internal.SearchOperationsImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import com.visenze.visearch.internal.json.ViSearchModule;

import java.net.URL;
import java.util.List;

/**
 * ViSearch APIs.
 *
 * See online documentation for <a href="http://www.visenze.com/doc/api/data">Data API</a>
 * and <a href="http://www.visenze.com/doc/api/search">Search API</a>.
 *
 * viSearch.insert(...)
 * viSearch.remove(...)
 * viSearch.search(...)
 * viSearch.colorSearch(...)
 * viSearch.uploadSearch(...)
 */
public class ViSearch implements DataOperations, SearchOperations {

    private static final String API_ENDPOINT = "http://visearch.visenze.com";

    private final DataOperations dataOperations;
    private final SearchOperations searchOperations;

    public ViSearch(String accessKey, String secretKey) {
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(API_ENDPOINT, accessKey, secretKey);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    public ViSearch(String endpoint, String accessKey, String secretKey) {
        if (endpoint == null) {
            throw new ViSearchException("Endpoint is not specified");
        }
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(endpoint, accessKey, secretKey);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    public ViSearch(URL endpoint, String accessKey, String secretKey) {
        if (endpoint == null) {
            throw new ViSearchException("Endpoint is not specified");
        }
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(endpoint.toString(), accessKey, secretKey);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    @Override
    public InsertTransaction insert(List<Image> imageList) {
        return dataOperations.insert(imageList);
    }

    @Override
    public PagedResult<InsertTransaction> getStatus(Integer page, Integer limit) {
        return dataOperations.getStatus(page, limit);
    }

    @Override
    public InsertTransaction getStatus(String transactionId) {
        return dataOperations.getStatus(transactionId);
    }

    @Override
    public void remove(List<String> imNameList) {
        dataOperations.remove(imNameList);
    }

    @Override
    public PagedSearchResult search(SearchParams searchParams) {
        return searchOperations.search(searchParams);
    }

    @Override
    public PagedSearchResult colorSearch(ColorSearchParams colorSearchParams) {
        return searchOperations.colorSearch(colorSearchParams);
    }

    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams) {
        return searchOperations.uploadSearch(uploadSearchParams);
    }

    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings) {
        return searchOperations.uploadSearch(uploadSearchParams, resizeSettings);
    }

}
