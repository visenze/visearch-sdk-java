package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.visearch.internal.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import com.visenze.visearch.internal.json.ViSearchModule;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class ViSearch implements DataOperations, SearchOperations {

    public static String VISEACH_JAVA_SDK_VERSION;

    /**
     * Default ViSearch API base endpoint.
     */
    private static final String DEFAULT_VISEARCH_ENDPOINT = "http://visearch.visenze.com";

    static {
        // load properties values
        try {
            Properties prop = new Properties();
            InputStream inputStream = ViSearch.class.getClassLoader().getResourceAsStream("visearch.sdk.properties");
            if (inputStream != null) {
                prop.load(inputStream);
            }
            VISEACH_JAVA_SDK_VERSION = prop.getProperty("visearch.sdk.version", "1.3.0");
        } catch (Exception e){
           e.printStackTrace();
        }
    }

    /**
     * ViSearch Data API i.e. /insert, /insert/status, /remove.
     */
    private final DataOperations dataOperations;

    /**
     * ViSearch Search API i.e. /search, /uploadsearch, /colorsearch.
     */
    private final SearchOperations searchOperations;

    /**
     * Construct a ViSearch client to call the default ViSearch API endpoint with access key and secret key.
     *
     * @param accessKey ViSearch App access key
     * @param secretKey ViSearch App secret key
     */
    public ViSearch(String accessKey, String secretKey) {
        this(DEFAULT_VISEARCH_ENDPOINT, accessKey, secretKey);
    }

    /**
     * (For testing) stub constructor
     */
    public ViSearch(DataOperations dataOperations, SearchOperations searchOperations) {
        this.dataOperations = dataOperations;
        this.searchOperations = searchOperations;
    }

    /**
     * Construct a ViSearch client to call a ViSearch API endpoint of URL object, with access key and secret key.
     *
     * @param endpoint  custom ViSearch endpoint
     * @param accessKey ViSearch App access key
     * @param secretKey ViSearch App secret key
     */
    public ViSearch(URL endpoint, String accessKey, String secretKey) {
        this(endpoint.toString(), accessKey, secretKey);
    }

    /**
     * Construct a ViSearch client to call a ViSearch API endpoint, with access key and secret key.
     *
     * @param endpoint  the ViSearch API endpoint
     * @param accessKey ViSearch App access key
     * @param secretKey ViSearch App secret key
     */
    public ViSearch(String endpoint, String accessKey, String secretKey) {
        if (endpoint == null) {
            throw new IllegalArgumentException("ViSearch endpoint must not be null.");
        }
        if (endpoint.isEmpty()) {
            throw new IllegalArgumentException("ViSearch endpoint must not be empty.");
        }
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(endpoint, accessKey, secretKey);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    public ViSearch(String endpoint, String accessKey, String secretKey, ClientConfig clientConfig) {
        if (endpoint == null) {
            throw new IllegalArgumentException("ViSearch endpoint must not be null.");
        }
        if (endpoint.isEmpty()) {
            throw new IllegalArgumentException("ViSearch endpoint must not be empty.");
        }
        if (clientConfig == null) {
            throw new IllegalArgumentException("ClientConfig must not be null.");
        }
        ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(endpoint, accessKey, secretKey, clientConfig);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        this.dataOperations = new DataOperationsImpl(viSearchHttpClient, objectMapper);
        this.searchOperations = new SearchOperationsImpl(viSearchHttpClient, objectMapper);
    }

    /**
     * Insert images to the ViSearch App.
     *
     * @param imageList the list of Images to insert.
     * @return an insert transaction
     */
    @Override
    public InsertTrans insert(List<Image> imageList) {
        return dataOperations.insert(imageList);
    }

    /**
     * (For testing) Insert images to the ViSearch App with custom parameters.
     *
     * @param imageList    the list of Images to insert.
     * @param customParams custom parameters
     * @return an insert transaction
     */
    @Override
    public InsertTrans insert(List<Image> imageList, Map<String, String> customParams) {
        return dataOperations.insert(imageList, customParams);
    }

    /**
     * Get insert status by insert trans id.
     *
     * @param transId the id of the insert trans.
     * @return the insert trans
     */
    @Override
    public InsertStatus insertStatus(String transId) {
        return dataOperations.insertStatus(transId);
    }

    /**
     * (For testing) Get insert status by insert trans id.
     *
     * @param transId the id of the insert trans.
     * @return the insert trans
     */
    @Override
    public InsertStatus insertStatus(String transId, Map<String, String> customParams) {
        return dataOperations.insertStatus(transId, customParams);
    }

    /**
     * Get insert status by insert trans id, and get errors page.
     *
     * @param transId    the id of the insert transaction.
     * @param errorPage  page number of the error list
     * @param errorLimit per page limit number of the error list
     * @return the insert transaction
     */
    @Override
    public InsertStatus insertStatus(String transId, Integer errorPage, Integer errorLimit) {
        return dataOperations.insertStatus(transId, errorPage, errorLimit);
    }

    /**
     * (For testing) Get insert status by insert trans id, and get errors page.
     *
     * @param transId    the id of the insert transaction.
     * @param errorPage  page number of the error list
     * @param errorLimit per page limit number of the error list
     *
     * @return the insert transaction
     */
    @Override
    public InsertStatus insertStatus(String transId, Integer errorPage, Integer errorLimit, Map<String, String> customParams) {
        return dataOperations.insertStatus(transId, errorPage, errorLimit, customParams);
    }



    /**
     * Remove a list of images from the ViSearch App, identified by their im_names.
     *
     * @param imNameList the list of im_names of the images to be removed
     * @return the remove status
     */
    @Override
    public RemoveStatus remove(List<String> imNameList) {
        return dataOperations.remove(imNameList);
    }

    /**
     * Search for similar images from the ViSearch App given an existing image in the App.
     *
     * @param searchParams the search parameters, must contain the im_name of the existing image
     * @return the page of search result
     */
    @Override
    public PagedSearchResult search(SearchParams searchParams) {
        return searchOperations.search(searchParams);
    }

    /**
     * Recommendation for similar images from the ViSearch App given an existing image in the App.
     * @param searchParams
     * @return
     */
    public PagedSearchResult recommendation(SearchParams searchParams) {
        PagedSearchResult result = searchOperations.recommendation(searchParams);
        return result;
    }

    /**
     * Search for similar images from the ViSearch App given a hex color.
     *
     * @param colorSearchParams the color search parameters, must contain the hex color
     * @return the page of color search result
     */
    @Override
    public PagedSearchResult colorSearch(ColorSearchParams colorSearchParams) {
        PagedSearchResult result = searchOperations.colorSearch(colorSearchParams);
        return result;
    }

    /**
     * Search for similar images from the ViSearch App given an image file or url.
     *
     * @param uploadSearchParams the upload search parameters, must contain a image file or a url
     * @return the page of upload search result
     */
    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams) {
        PagedSearchResult result = searchOperations.uploadSearch(uploadSearchParams);
        return result;
    }

    /**
     * Detect multiple objects and search for similar images from the ViSearch App
     * The input will be an image file or url.
     *
     * @param discoverSearchParams the upload search parameters, must contain a image file or a url
     * @return the page of upload search result
     */
    @Override
    public PagedSearchResult discoverSearch(UploadSearchParams discoverSearchParams) {
        PagedSearchResult result = searchOperations.discoverSearch(discoverSearchParams);
        return result;
    }

    /**
     * Detect multiple objects and search for similar images from the ViSearch App
     * The input will be an image file or url.
     *
     * @param similarProductsSearchParams the upload search parameters, must contain a image file or a url
     * @return the page of upload search result
     */
    @Override
    @Deprecated
    public PagedSearchResult similarProductsSearch(UploadSearchParams similarProductsSearchParams) {
        PagedSearchResult result = searchOperations.similarProductsSearch(similarProductsSearchParams);
        return result;
    }

    /**
     * Extract feature string (encoded in base 64) given an image file or url.
     *
     * @param uploadSearchParams the upload search parameters, must contain a image file or a url
     * @return the feature response string result
     */
    @Override
    public FeatureResponseResult extractFeature(UploadSearchParams uploadSearchParams) {
        FeatureResponseResult result = searchOperations.extractFeature(uploadSearchParams);
        return result;
    }

    /**
     * Search by all objects for similar images from the ViSearch App given an existing image in the App.
     *
     * @param matchSearchParams the search parameters, must contain the im_name of the existing image
     * @return the page of search result
     */
    @Override
    public PagedSearchResult matchSearch(MatchSearchParams matchSearchParams) {
        PagedSearchResult result = searchOperations.matchSearch(matchSearchParams);
        return result;
    }

}
