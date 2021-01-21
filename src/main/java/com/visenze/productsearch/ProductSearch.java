package com.visenze.productsearch;

import com.visenze.common.http.ViHttpClient;
import com.visenze.common.http.ViHttpResponse;
import com.visenze.productsearch.param.*;
import com.visenze.visearch.ClientConfig;
import org.jetbrains.annotations.NotNull;

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
    public static final String DEFAULT_ENDPOINT = "http://visearch.visenze.com";

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
    private ViHttpClient httpClient;

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
         * @param app_key
         * @param placement_id
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
    private ProductSearch(String app_key, Integer placement_id, String endpoint, ClientConfig config) {
        this.app_key      = app_key;
        this.placement_id = placement_id;
        this.endpoint     = endpoint;
        this.httpClient   = new ViHttpClient(config, app_key, placement_id.toString());
    }

    /**
     * WIP untill the endpoints are confirmed
     * @param params
     * @return
     */
    public ViHttpResponse imageSearch(ImageSearchParam params) {
        // required field is automatically set here
        params.setAppKey(app_key);
        params.setPlacementId(placement_id);
        return httpClient.post(endpoint + "/similar-products", params.toMultimap());
    }

    /**
     * WIP untill the endpoints are confirmed
     * @param params
     * @return
     */
    public ViHttpResponse visualSimilarSearch(VisualSimilarParam params) {
        // required field is automatically set here
        params.setAppKey(app_key);
        params.setPlacementId(placement_id);
        return httpClient.post(endpoint + "/similar-products", params.toMultimap());
    }






}
