package com.visenze.productsearch.response;

import com.visenze.productsearch.ProductSearchResponse;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * <h1> Error Type Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 20 Jan 2021
 */
public class ErrorMsgTest {
    final String jsonFormat = "{\"error\":{\"code\": 100, \"message\": \"Parameter 'im_url' is required\"}}";
    /**
     * Parsing JSON formatted string
     */
    @Test
    public void testJsonDeserializing() {
        ViSearchHttpResponse mockedHttpResponse = mock(ViSearchHttpResponse.class);
        when(mockedHttpResponse.getBody()).thenReturn(jsonFormat);

        ProductSearchResponse response = ProductSearchResponse.fromResponse(mockedHttpResponse);
        ErrorMsg errorMsg = response.getError();

        assertEquals(100, errorMsg.getCode().intValue());
        assertEquals("Parameter 'im_url' is required", errorMsg.getMessage());
    }
}