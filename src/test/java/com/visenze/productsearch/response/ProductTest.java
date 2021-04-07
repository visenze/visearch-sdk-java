package com.visenze.productsearch.response;

import com.visenze.common.util.ViJsonAny;
import com.visenze.productsearch.ProductSearchResponse;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <h1> Product Info Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 20 Jan 2021
 */
public class ProductTest {
    final String JSON_KNOWN = "{\"result\":[{\"product_id\":\"PRODUCT_ID\",\"main_image_url\":\"IMAGE_URL\",\"score\":0.827}]}";
    final String JSON_UNKNOWN = "{\"result\":[{\"data\":{\"sku\":1234,\"product_name\":\"PRODUCT_NAME\",\"sale_price\":{\"currency\":\"SGD\",\"value\":\"120\"},\"color\":[\"blue\",\"red\",\"green\"]}}]}";
    final String JSON_PRODUCT = "{\"result\":[{\"product_id\":\"PRODUCT_ID\",\"main_image_url\":\"IMAGE_URL\",\"score\":0.827,\"data\":{\"sku\":1234,\"product_name\":\"PRODUCT_NAME\",\"sale_price\":{\"currency\":\"SGD\",\"value\":\"120\"},\"color\":[\"blue\",\"red\",\"green\"]}}]}";
    /**
     * Verifies known data fields - all members except 'data'. Checks are based
     * on values of jsonFormat
     *
     * @param info ProductInfo to verify
     */
    private void verifyKnownOnly(Product info) {
        assertEquals("PRODUCT_ID", info.getProductId());
        assertEquals("IMAGE_URL", info.getMainImageUrl());
        assertEquals(0.827, info.getScore(), 0.0001f);
    }

    /**
     * Verifies unknown data fields - only 'data' member. Checks are based on
     * values of jsonFormatData
     *
     * @param info ProductInfo to verify
     */
    private void verifyUnknownOnly(Product info) {
        Map<String, ViJsonAny> mappedData = info.getData();
        assertEquals(1234, (int)mappedData.get("sku").asInteger());
        assertEquals("PRODUCT_NAME", mappedData.get("product_name").asString());

        Map<String, String> price_map = mappedData.get("sale_price").asStringStringMap();
        assertEquals(2, price_map.size());
        assertEquals("SGD", price_map.get("currency"));
        assertEquals("120", price_map.get("value"));

        List<String> colors = mappedData.get("color").asStringList();
        assertEquals(3, colors.size());
        assertEquals("blue", colors.get(0));
        assertEquals("red", colors.get(1));
        assertEquals("green", colors.get(2));
    }

    @Test
    public void testJsonDeserializingKnown() {
        ViSearchHttpResponse mockedHttpResponse = mock(ViSearchHttpResponse.class);
        when(mockedHttpResponse.getBody()).thenReturn(JSON_KNOWN);
        ProductSearchResponse response = ProductSearchResponse.fromResponse(mockedHttpResponse);
        Product info = response.getResult().get(0);
        verifyKnownOnly(info);
    }

    @Test
    public void testJsonDeserializingUnknown() {
        ViSearchHttpResponse mockedHttpResponse = mock(ViSearchHttpResponse.class);
        when(mockedHttpResponse.getBody()).thenReturn(JSON_UNKNOWN);
        ProductSearchResponse response = ProductSearchResponse.fromResponse(mockedHttpResponse);
        Product info = response.getResult().get(0);
        verifyUnknownOnly(info);
    }

    @Test
    public void testJsonDeserializingSimulate() {
        ViSearchHttpResponse mockedHttpResponse = mock(ViSearchHttpResponse.class);
        when(mockedHttpResponse.getBody()).thenReturn(JSON_PRODUCT);
        ProductSearchResponse response = ProductSearchResponse.fromResponse(mockedHttpResponse);
        Product info = response.getResult().get(0);
        verifyKnownOnly(info);
        verifyUnknownOnly(info);
    }
}