package com.visenze.productsearch;

import com.google.common.base.Preconditions;
import com.visenze.productsearch.param.*;
import com.visenze.visearch.ClientConfig;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <h1> ProductSearch </h1>
 * This class wraps ViSearch internally to help delegate tasks and re-use
 * modular pieces that were included in the com.visenze.visearch package. Since
 * This class is located in separate package, there will be a lot of dependency
 * from the .visearch package.
 * <p>
 * Future plan? To split things that are
 * non-api specific into a 'common' or 'util' package for both ProductSearch and
 * ViSearch to use.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class ProductSearch {

    /**
     * Default endpoint if none is set
     */
    static final String DEFAULT_ENDPOINT = "https://search-dev.visenze.com//v1";
    static final String DEFAULT_IMAGE_SEARCH_PATH = "/similar-products";
    static final String DEFAULT_VISUAL_SIMILAR_PATH = "/similar-products";

    /**
     * App key, required field that also acts as authentication element
     */
    private String app_key;

    /**
     * Placement ID, required field that helps identify which placement template
     */
    private Integer placement_id;

    /**
     * Endpoint to use for all ViHttpClient calls
     */
    private String endpoint;

    /**
     * The Http wrapper class for easy functionalities
     */
    private ViSearchHttpClientImpl httpClient;

    /**
     * Builder class to make configuring of ProductSearch more readable
     */
    public static class Builder{

        /**
         * Similar to ProductSearch
         *
         * @see ProductSearch
         */
        private String app_key;

        /**
         * Similar to ProductSearch
         *
         * @see ProductSearch
         */
        private Integer placement_id;

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
            this.app_key      = app_key;
            this.placement_id = placement_id;
            this.config       = new ClientConfig();
        }

        /**
         * Build the ProductSearch class based on configurations set through the
         * builder pattern.
         *
         * @return ProductSearch object class
         */
        public ProductSearch build() {
            if (this.app_key == null || this.app_key.isEmpty()) {
                throw new InternalViSearchException(ResponseMessages.INVALID_KEY);
            }
            return new ProductSearch(this.app_key, this.placement_id, endpoint, config);
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
     * @param app_key Unique app key that acts as authenticator for the client
     * @param placement_id Placement ID of the template that was chosen when
     *                     creating apps on the dashboard
     * @param endpoint Which endpoint to use for all ViHttpClient queries
     * @param config Configuration to the behaviours of the ViHttpClient
     */
    private ProductSearch(String app_key, Integer placement_id, String endpoint,
                          ClientConfig config)
    {
        this.app_key      = app_key;
        this.placement_id = placement_id;
        this.endpoint     = endpoint;
        this.httpClient   = new ViSearchHttpClientImpl(this.endpoint, app_key, placement_id.toString(), config);
    }

    /**
     * Calls the POST method for the image search API
     *
     * @param params ImageSearchParam specific parameters to perform request
     *
     * @return ViSearchHttpResponse http response of search results
     */
    public ViSearchHttpResponse imageSearch(ImageSearchParam params) {
        // required field is automatically set here
        params.setAppKey(app_key);
        params.setPlacementId(placement_id);
        // test for image parameter validity
        final File imageFile = params.getImage();
        final String imageID = params.getImageId();
        final String imageURL = params.getImageUrl();
        final boolean invalidID = imageID == null || imageID.isEmpty();
        final boolean invalidURL = imageURL == null || imageURL.isEmpty();
        if (imageFile == null && invalidID && invalidURL)
            throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_SOURCE);
        // attempt search using image file
        if (imageFile != null) {
            try {
                return httpClient.postImage(DEFAULT_IMAGE_SEARCH_PATH, params.toMultimap(), new FileInputStream(imageFile), imageFile.getName());
            } catch (FileNotFoundException e) {
                throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_OR_URL, e);
            }
        }
        // attempt using post for image url or image id
        return httpClient.post(DEFAULT_IMAGE_SEARCH_PATH, params.toMultimap());
    }

    /**
     * Calls the GET method for the API which appends the product_id to the path
     *
     * @param params VisualSimilarParam specific parameters to perform request
     *
     * @return ViSearchHttpResponse http response of search results
     */
    public ViSearchHttpResponse visualSimilarSearch(VisualSimilarParam params) {
        // required field is automatically set here
        params.setAppKey(app_key);
        // append the product id after the visual similar path
        final String path = DEFAULT_VISUAL_SIMILAR_PATH + '/' + params.getProductId();
        return httpClient.get(path, params.toMultimap());
    }






}
