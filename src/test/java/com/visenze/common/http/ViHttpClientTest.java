package com.visenze.common.http;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.visenze.visearch.ClientConfig;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * <h1> ViHttpClientTest Unit Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 15 Jan 2021
 */
public class ViHttpClientTest extends TestCase {
    private static final String test_endpoint = "https://search-dev.visenze.com//v1/similar-products";
    private static final String dummy_acces_key = "";
    private static final String dummy_secret_key = "1";

    /**
     * Test GET api
     */
    @Test
    public void testGet() {
        ViHttpClient client = new ViHttpClient(new ClientConfig(), dummy_acces_key, dummy_secret_key);
        ViHttpResponse response = client.get(test_endpoint, getParameters(dummy_acces_key, dummy_secret_key));
        response.getBody();

    }

    /**
     * Test POST api
     */
    @Test
    public void testPost() {

    }

    /**
     * Get a dummy data multimap to test as parameters.
     */
    private Multimap<String, String> getParameters(String app_key, String placement_id) {
        Multimap<String, String> params = HashMultimap.create();
        params.put("app_key",app_key);
        params.put("placement_id",placement_id);
        params.put("key_1","val_1");
        params.put("key_2","val_2");
        params.put("key_2","2");
        params.put("key_3","val_3");
        params.put("key_3","3");
        params.put("key_3","33");
        return params;
    }
}