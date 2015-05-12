package com.visenze.visearch;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EncodingUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ViSearchHttpClientTest {
    private String validEndpoint = "http://localhost/";
    private String validAccessKey = "$%&valid_access key-123";
    private String validSecretKey = "validRANDOMsecrete#!34key";
    private String invalidEndpoint = "invalid url";
    private String invalidAccessKey = null;
    private String path = "";
    private Multimap<String, String> params = ArrayListMultimap.create();
    private CloseableHttpClient mockedHttpClient = mock(CloseableHttpClient.class);

    @Test
    public void testValidGetMethod() {
        ViSearchHttpClientImpl client = new ViSearchHttpClientImpl(validEndpoint, validAccessKey, validSecretKey, mockedHttpClient);
        ArgumentCaptor<HttpUriRequest> argument = ArgumentCaptor.forClass(HttpUriRequest.class);

        try {
            CloseableHttpResponse response = mock(CloseableHttpResponse.class);
            when(response.getEntity()).thenReturn(new StringEntity("test"));
            when(mockedHttpClient.execute(argument.capture())).thenReturn(response);
            client.get(path, params);
        } catch (IOException e) {
            assert(false);  // should not be executed
        }

        HttpUriRequest request = argument.getValue();
        Header[] headerArray = request.getAllHeaders();
        String expected = "Basic " + EncodingUtils.getAsciiString(Base64.encodeBase64(EncodingUtils.getAsciiBytes(validAccessKey + ":" + validSecretKey)));

        boolean isFound = false;
        for (int i=0; i<headerArray.length; i++) {
            if (headerArray[i].getValue().equals(expected)) {
                isFound = true; // found credentials
                break;
            }
        }
        assertTrue(isFound);
    }

    @Test
    public void testInvalidGetMethod() {
        ViSearchHttpClientImpl client;

        // invalid endpoint
        try {
            client = new ViSearchHttpClientImpl(invalidEndpoint, validAccessKey, validSecretKey, mockedHttpClient);
            client.get(path, params);
            assert(false); // should not be executed
        } catch (Exception e) {
            assertTrue(e instanceof ViSearchException);
        }

        // invalid access key
        try {
            client = new ViSearchHttpClientImpl(validEndpoint, invalidAccessKey, validSecretKey, mockedHttpClient);
            client.get(path, params);
            assert(false); // should not be executed
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

        // combination of invalid endpoint and invalid access key
        try {
            client = new ViSearchHttpClientImpl(invalidEndpoint, invalidAccessKey, validSecretKey, mockedHttpClient);
            client.get(path, params);
            assert(false); // should not be executed
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException); // the first encountered exception
        }
    }
}
