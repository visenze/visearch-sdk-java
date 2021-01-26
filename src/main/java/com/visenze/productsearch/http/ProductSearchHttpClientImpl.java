package com.visenze.productsearch.http;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.visenze.visearch.ClientConfig;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.apache.http.Header;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

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

    /**
     * We override so that the method will use our modified getResponse
     *
     * @param path endpoint path
     * @param params parameters
     * @return api response
     */
    @Override
    public ViSearchHttpResponse get(String path, Multimap<String, String> params) {
        HttpUriRequest request = buildGetRequest(endpoint + path, params);
        return getResponse(request);
    }

    /**
     * We override so that the method will use our modified getResponse
     *
     * @param path endpoint path
     * @param params parameters
     * @return api response
     */
    @Override
    public ViSearchHttpResponse post(String path, Multimap<String, String> params) {
        HttpUriRequest request = buildPostRequest(endpoint + path, params);
        return getResponse(request);
    }

    /**
     * We override so that the method will use our modified getResponse
     *
     * @param path endpoint path
     * @param params parameters
     * @param file Image file object
     * @return api response
     */
    @Override
    public ViSearchHttpResponse postImage(String path, Multimap<String, String> params, File file) {
        HttpUriRequest request = buildPostRequestForImage(endpoint + path, params, file);
        return getResponse(request);
    }

    /**
     * We implement a duplicate of the base class method omitting the
     * addAuthHeader as ProductSearch does not need it
     *
     * @param request http request
     * @return api response
     */
    private ViSearchHttpResponse getResponse(HttpUriRequest request) {
        addOtherHeaders(request);
        CloseableHttpResponse response = executeRequest(request);
        try {
            Map<String, String> headers = Maps.newHashMap();
            Header[] responseHeaders = response.getAllHeaders();
            if (responseHeaders != null) {
                for (Header header : responseHeaders) {
                    headers.put(header.getName(), header.getValue());
                }
            }
            ViSearchHttpResponse response1 = new ViSearchHttpResponse(response);
            response1.setHeaders(headers);
            return response1;
        } catch (IllegalArgumentException e) {
            throw new InternalViSearchException(ResponseMessages.SYSTEM_ERROR, e);
            // throw new NetworkException("A network error occurred when reading response from the ViSearch endpoint. " +
            //        "Please check your network connectivity and try again.", e);
        }
    }
}
