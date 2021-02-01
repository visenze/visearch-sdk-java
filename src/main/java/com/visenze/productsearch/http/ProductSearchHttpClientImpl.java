package com.visenze.productsearch.http;

import com.visenze.visearch.ClientConfig;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * <h1> ProductSearchHttpClientImpl </h1>
 * This class extends the ViSearchHttpClientImpl class to reuse most of its
 * functionalities. Since ProductSearch no longer needs authentication in its
 * headers (app key and placement id is passed as parameters), the getResponse
 * method implemented here is the same as ViSearchHttpClientImpl except for the
 * addAuthHeader method not being called.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 26 Jan 2021
 */
public class ProductSearchHttpClientImpl extends ViSearchHttpClientImpl {

    public ProductSearchHttpClientImpl(String endpoint, ClientConfig config) {
        super(endpoint, "", "", config);
    }

    @Override
    protected ViSearchHttpResponse getResponse(HttpUriRequest request) {
        // for Product SEarch, auth is via app_key
        addOtherHeaders(request);
        return getViSearchHttpResponse(request);
    }
}
