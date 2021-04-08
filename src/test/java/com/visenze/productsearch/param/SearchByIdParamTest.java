package com.visenze.productsearch.param;

import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;
/**
 * <h1> Search by image ID Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 18 Mar 2021
 */
public class SearchByIdParamTest {

    final String PARAM_OUTPUT = "return_product_info=true";
    final String PRODUCT_ID_1 = "12345";
    final String PRODUCT_ID_2 = "67890";

    @Test
    public void toMap() {
        SearchByIdParam param = new SearchByIdParam(PRODUCT_ID_1);
        assertEquals(PRODUCT_ID_1, param.getProductId());

        param.setReturnProductInfo(true);
        assertEquals(true, param.getReturnProductInfo());

        param.setProductId(PRODUCT_ID_2);
        assertEquals(PRODUCT_ID_2, param.getProductId());

        List<NameValuePair> paramUrl = ViSearchHttpClientImpl.mapToNameValuePair(param.toMultimap());
        try{
            URI uri = new URIBuilder("www.example.com").addParameters(paramUrl).build();
            assertEquals(PARAM_OUTPUT, uri.getQuery());
        } catch (URISyntaxException e) {
            fail("Failed to create proper query");
        }
    }
}