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


public class ViSearch implements DataOperations, SearchOperations {

    /**
     * Default ViSearch API base endpoint.
     */
    private static final String API_ENDPOINT = "http://visearch.visenze.com";

    /**
     * ViSearch Data API i.e. /insert, /insert/status, /remove.
     */
    private final DataOperations dataOperations;

    /**
     * ViSearch Search API i.e. /search, /uploadsearch, /colorsearch.
     */
    private final SearchOperations searchOperations;

    /**
     * Construct a ViSearch client to call the default endpoint with access key and secret key.
     * @param accessKey ViSearch App access key
     * @param secretKey ViSearch App secret key
     */
    public ViSearch(String accessKey, String secretKey) {
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(API_ENDPOINT, accessKey, secretKey);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    /**
     * (For testing) Construct a ViSearch client to call a custom endpoint with access key and secret key.
     * @param endpoint custom ViSearch endpoint
     * @param accessKey ViSearch App access key
     * @param secretKey ViSearch App secret key
     */
    public ViSearch(String endpoint, String accessKey, String secretKey) {
        if (endpoint == null) {
            throw new ViSearchException("Endpoint is not specified");
        }
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(endpoint, accessKey, secretKey);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    /**
     * (For testing) Construct a ViSearch client to call a custom endpoint with access key and secret key.
     * @param endpoint custom ViSearch endpoint
     * @param accessKey ViSearch App access key
     * @param secretKey ViSearch App secret key
     */
    public ViSearch(URL endpoint, String accessKey, String secretKey) {
        if (endpoint == null) {
            throw new ViSearchException("Endpoint is not specified");
        }
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(endpoint.toString(), accessKey, secretKey);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    /**
     * Insert images to the ViSearch App.
     * @param imageList the list of Images to insert.
     * @return an insert transaction
     */
    @Override
    public InsertTrans insert(List<Image> imageList) {
        return dataOperations.insert(imageList);
    }

    /**
     * Get insert status by insert trans id.
     * @param transId the id of the insert trans.
     * @return the insert trans
     */
    @Override
    public InsertStatus insertStatus(String transId) {
        return dataOperations.insertStatus(transId);
    }

    /**
     * Get insert status by insert trans id, and get errors page.
     * @param transId the id of the insert transaction.
     * @param errorPage page number of the error list
     * @param errorLimit per page limit number of the error list
     * @return the insert transaction
     */
    @Override
    public InsertStatus insertStatus(String transId, Integer errorPage, Integer errorLimit) {
        return dataOperations.insertStatus(transId, errorPage, errorLimit);
    }

    /**
     * Remove a list of images from the ViSearch App, identified by their im_names.
     * @param imNameList the list of im_names of the images to be removed
     */
    @Override
    public void remove(List<String> imNameList) {
        dataOperations.remove(imNameList);
    }

    /**
     * Search for similar images from the ViSearch App given an existing image in the App.
     * @param searchParams the search parameters, must contain the im_name of the existing image
     * @return the page of search result
     */
    @Override
    public PagedSearchResult search(SearchParams searchParams) {
        return searchOperations.search(searchParams);
    }

    /**
     * Search for similar images from the ViSearch App given a hex color.
     * @param colorSearchParams the color search parameters, must contain the hex color
     * @return the page of color search result
     */
    @Override
    public PagedSearchResult colorSearch(ColorSearchParams colorSearchParams) {
        return searchOperations.colorSearch(colorSearchParams);
    }

    /**
     * Search for similar images from the ViSearch App given an image file or url.
     * @param uploadSearchParams the upload search parameters, must contain a image file or a url
     * @return the page of upload search result
     */
    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams) {
        return searchOperations.uploadSearch(uploadSearchParams);
    }

    /**
     * Search for similar images from the ViSearch App given an image file or url, with customized image resize option.
     * @param uploadSearchParams the upload search parameters, must contain a image file or a url
     * @param resizeSettings custom image resize option
     * @return the page of upload search result
     */
    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings) {
        return searchOperations.uploadSearch(uploadSearchParams, resizeSettings);
    }

}
