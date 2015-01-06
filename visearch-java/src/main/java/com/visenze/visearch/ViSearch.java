package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.visearch.internal.DataOperations;
import com.visenze.visearch.internal.SearchOperations;
import com.visenze.visearch.internal.ViSearchImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import com.visenze.visearch.internal.json.ViSearchModule;

/**
 * ViSearch APIs.
 * <p/>
 * See online documentation for <a href="http://www.visenze.com/doc/api/data">Data API</a>
 * and <a href="http://www.visenze.com/doc/api/search">Search API</a>.
 * <p/>
 * viSearch.insert(...)
 * viSearch.remove(...)
 * viSearch.search(...)
 * viSearch.colorSearch(...)
 * viSearch.uploadSearch(...)
 */
public interface ViSearch extends DataOperations, SearchOperations {

    public static class Builder {

        private static final String API_ENDPOINT = "http://visearch.visenze.com";

        private String accessKey;
        private String secretKey;

        private Builder() {
        }

        private Builder(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String accessKey, String secretKey) {
            return new Builder(accessKey, secretKey);
        }

        public Builder withCredentials(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            return this;
        }

        public Builder withAccessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        public Builder withSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public ViSearch build() {
            ViSearchHttpClient viSearchHttpClient = new ViSearchHttpClientImpl(accessKey, secretKey);
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
            return new ViSearchImpl(viSearchHttpClient, objectMapper, API_ENDPOINT);
        }
    }

}
