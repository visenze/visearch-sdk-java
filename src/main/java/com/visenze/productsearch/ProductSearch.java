package com.visenze.productsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Multimap;
import com.visenze.productsearch.http.ProductSearchHttpClientImpl;
import com.visenze.productsearch.param.*;
import com.visenze.visearch.ClientConfig;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1> ProductSearch </h1>
 * This class wraps ViSearch internally to help delegate tasks and re-use
 * modular pieces that were included in the com.visenze.visearch package. Since
 * This class is located in separate package, there will be a lot of dependency
 * from the .visearch package.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class ProductSearch {
    /**
     * Default endpoint if none is set
     */
    static final String DEFAULT_ENDPOINT = "https://multimodal.search.rezolve.com";
    static final String ENDPOINT_AWS = "https://multisearch-aw.rezolve.com";
    static final String ENDPOINT_AZURE = "https://multisearch-az.rezolve.com";

    public static final String APP_KEY = "app_key";
    public static final String PLACEMENT_ID = "placement_id";

    private static final Set<String> NEW_ENDPOINTS = new HashSet<String>(Arrays.asList(
            ENDPOINT_AWS,
            ENDPOINT_AZURE
    ));

    static final EndpointPathConfig LEGACY_PATHS = new EndpointPathConfig(
            "/v1/product/search_by_image",
            "/v1/product/multisearch",
            "/v1/product/multisearch/autocomplete",
            "/v1/product/search_by_id",
            "/v1/product/recommendations"
    );

    static final EndpointPathConfig NEW_PATHS = new EndpointPathConfig(
            "/v1/visearch/search_by_image",
            "/v1/search",
            "/v1/autocomplete",
            "/v1/visearch/search_by_id",
            "/v1/visearch/recommendations"
    );

    static class EndpointPathConfig {
        final String imageSearchPath;
        final String multiSearchPath;
        final String multiSearchAutocompletePath;
        final String visualSimilarPath;
        final String recommendationPath;

        EndpointPathConfig(String imageSearchPath, String multiSearchPath,
                           String multiSearchAutocompletePath, String visualSimilarPath,
                           String recommendationPath) {
            this.imageSearchPath = imageSearchPath;
            this.multiSearchPath = multiSearchPath;
            this.multiSearchAutocompletePath = multiSearchAutocompletePath;
            this.visualSimilarPath = visualSimilarPath;
            this.recommendationPath = recommendationPath;
        }
    }

    private static boolean isNewEndpoint(String endpoint) {
        return NEW_ENDPOINTS.contains(endpoint.replaceAll("/$", ""));
    }

    /**
     * App key, required field that also acts as authentication element
     */
    @JsonProperty("app_key")
    private String appKey;

    /**
     * Placement ID, required field that helps identify which placement template
     */
    @JsonProperty("placement_id")
    private Integer placementId;

    /**
     * Endpoint to use for all ViHttpClient calls
     */
    private String endpoint;

    /**
     * API paths selected based on the configured endpoint
     */
    private EndpointPathConfig pathConfig;

    /**
     * The Http wrapper class for easy functionalities
     */
    private ProductSearchHttpClientImpl httpClient;

    /**
     * Builder class to make configuring of ProductSearch more readable
     */
    public static class Builder{

        /**
         * Similar to ProductSearch
         *
         * @see ProductSearch
         */
        private String appKey;

        /**
         * Similar to ProductSearch
         *
         * @see ProductSearch
         */
        private Integer placementId;

        /**
         * Similar to ProductSearch, defaulted to the default endpoint
         *
         * @see ProductSearch
         */
        private String endpoint = DEFAULT_ENDPOINT;

        /**
         * Client configurations such as time-outs etc. These configurations
         * will be used to construct the ViHttpClient class that is stored as
         * member variable of ProductSearch
         *
         * @see ClientConfig
         * @see ProductSearch
         */
        private ClientConfig config;

        /**
         * Constructor with only highly necessary parameters.
         *
         * @param app_key unique access key used as credentials
         * @param placement_id which placement ID (from dashboard)
         */
        public Builder(String app_key, Integer placement_id) {
            this.appKey      = app_key;
            this.placementId = placement_id;
            this.config       = new ClientConfig();
        }

        /**
         * Build the ProductSearch class based on configurations set through the
         * builder pattern.
         *
         * @return ProductSearch object class
         */
        public ProductSearch build() {
            if (this.appKey == null || this.appKey.isEmpty()) {
                throw new InternalViSearchException(ResponseMessages.INVALID_KEY);
            }
            return new ProductSearch(this.appKey, this.placementId, endpoint, config);
        }

        /**
         * Set api custom url endpoint for
         *
         * @param url The custom url endpoint to use instead
         *
         * @return this 'itself'
         */
        public Builder setApiEndPoint(String url) {
            this.endpoint = url;
            return this;
        }

        /**
         * Use the AWS Rezolve endpoint (https://multisearch-aw.rezolve.com)
         *
         * @return this 'itself'
         */
        public Builder useAws() {
            this.endpoint = ENDPOINT_AWS;
            return this;
        }

        /**
         * Use the Azure Rezolve endpoint (https://multisearch-az.rezolve.com)
         *
         * @return this 'itself'
         */
        public Builder useAzure() {
            this.endpoint = ENDPOINT_AZURE;
            return this;
        }

        /**
         * Set custom client configurations for http connections
         *
         * @param config custom client http configurations
         *
         * @return this 'itself'
         *
         * @see ClientConfig
         */
        public Builder setClientConfig(ClientConfig config) {
            this.config = config;
            return this;
        }
    }

    /**
     * Constructor of ProductSearch made private to only allow it's Builder
     * class to create it.
     *
     * @param appKey Unique app key that acts as authenticator for the client
     * @param placementId Placement ID of the template that was chosen when
     *                     creating apps on the dashboard
     * @param endpoint Which endpoint to use for all ViHttpClient queries
     * @param config Configuration to the behaviours of the ViHttpClient
     */
    private ProductSearch(String appKey, Integer placementId, String endpoint,
                          ClientConfig config)
    {
        this.appKey       = appKey;
        this.placementId  = placementId;
        this.endpoint     = endpoint;
        this.pathConfig   = isNewEndpoint(endpoint) ? NEW_PATHS : LEGACY_PATHS;
        this.httpClient   = new ProductSearchHttpClientImpl(this.endpoint, config);
    }

    public ProductSearchResponse multiSearch(SearchByImageParam params) {
        return postImageSearch(params, pathConfig.multiSearchPath);
    }

    public AutoCompleteResponse multiSearchAutocomplete(SearchByImageParam params) {
        Multimap<String, String> paramMap = addAuth2Map(params);

        final File imageFile = params.getImage();

        if (imageFile != null) {
            try {
                return AutoCompleteResponse.fromResponse(httpClient.postImage(pathConfig.multiSearchAutocompletePath, paramMap, new FileInputStream(imageFile), imageFile.getName()));
            } catch (FileNotFoundException e) {
                throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_OR_URL, e);
            }
        }

        return AutoCompleteResponse.fromResponse(httpClient.post(pathConfig.multiSearchAutocompletePath, paramMap));
    }

    /**
     * Calls the POST method for the image search API
     *
     * @param params ImageSearchParam specific parameters to perform request
     *
     * @return ViSearchHttpResponse http response of search results
     */
    public ProductSearchResponse imageSearch(SearchByImageParam params) {
        return postImageSearch(params, pathConfig.imageSearchPath);
    }

    private ProductSearchResponse postImageSearch(SearchByImageParam params, String apiPath) {
        Multimap<String, String> paramMap = addAuth2Map(params);

        final File imageFile = params.getImage();
        // attempt search using image file
        if (imageFile != null) {
            try {
                return ProductSearchResponse.fromResponse(httpClient.postImage(apiPath, paramMap, new FileInputStream(imageFile), imageFile.getName()));
            } catch (FileNotFoundException e) {
                throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_OR_URL, e);
            }
        }

        // attempt using post for image url or image id
        return ProductSearchResponse.fromResponse(httpClient.post(apiPath, paramMap));
    }

    /**
     * Calls the GET method for the API which appends the product_id to the path
     *
     * @param params VisualSimilarParam specific parameters to perform request
     *
     * @return ViSearchHttpResponse http response of search results
     */
    @Deprecated
    public ProductSearchResponse visualSimilarSearch(SearchByIdParam params) {
        final String path = pathConfig.visualSimilarPath + '/' + params.getProductId();
        Multimap<String, String> paramMap = addAuth2Map(params);
        return ProductSearchResponse.fromResponse(httpClient.get(path, paramMap));
    }

    /**
     * Calls the GET method for the API which appends the product_id to the path
     *
     * @param params VisualSimilarParam specific parameters to perform request
     *
     * @return ViSearchHttpResponse http response of search results
     */
    public ProductSearchResponse recommendations(SearchByIdParam params) {
        return recomendation(params);
    }


    @Deprecated
    public ProductSearchResponse recomendation(SearchByIdParam params) {
        final String path = pathConfig.recommendationPath + '/' + params.getProductId();
        Multimap<String, String> paramMap = addAuth2Map(params);
        return ProductSearchResponse.fromResponse(httpClient.get(path, paramMap));
    }

    /**
     * Add placement ID and app key to parameter for authentication since we
     * do not use any in basic auth header
     *
     * @param params search parameters
     * @return parameters with injected placement id and app key
     */
    private Multimap<String, String> addAuth2Map(BaseProductSearchParam params) {
        Multimap<String, String> paramMap = params.toMultimap();

        String appKeyParam = params.getAppKey() == null ? this.appKey : params.getAppKey();
        paramMap.put(APP_KEY, appKeyParam);

        Integer placementIdParam = params.getPlacementId() == null ? this.placementId : params.getPlacementId();
        paramMap.put(PLACEMENT_ID, placementIdParam.toString());
        return paramMap;
    }

    /**
     * Sets the http client to use. Meant to be used for testing - mocking, as
     * the default created on construction is sufficient.
     *
     * @param httpClient  http client to use
     */
    public void setHttpClient(ProductSearchHttpClientImpl httpClient) {
        this.httpClient = httpClient;
    }

}
