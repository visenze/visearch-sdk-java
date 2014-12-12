package com.visenze.visearch;

import com.visenze.visearch.internal.ViSearchClient;

/**
 * Factory for building an {@link com.visenze.visearch.ViSearch} instance.
 * <p/>
 * Example:
 * // builds a ViSearch instance with the accessKey and secretKey of your ViSearch app
 * ViSearch viSearch = ViSearchFactory(accessKey, secretKey).build();
 * <p/>
 * // call Data APIs and Search APIs
 * viSearch.insert(...)
 * viSearch.remove(...)
 * viSearch.search(...)
 * viSearch.colorSearch(...)
 * viSearch.uploadSearch(...)
 */
public class ViSearchFactory {

    private String accessKey;
    private String secretKey;

    private String dataApiUrl;
    private String searchApiUrl;

    public ViSearchFactory(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        dataApiUrl = "http://visearch.visenze.com";
        searchApiUrl = "http://visearch.visenze.com";
    }

    public ViSearch build() {
        return new ViSearchClient(accessKey, secretKey, dataApiUrl, searchApiUrl);
    }

    public ViSearchFactory setDataApiUrl(String dataApiUrl) {
        this.dataApiUrl = dataApiUrl;
        return this;
    }

    public ViSearchFactory setSearchApiUrl(String searchApiUrl) {
        this.searchApiUrl = searchApiUrl;
        return this;
    }

}
