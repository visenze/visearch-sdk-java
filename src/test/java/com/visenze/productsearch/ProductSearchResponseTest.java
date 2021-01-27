package com.visenze.productsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import com.visenze.productsearch.param.SearchByImageParam;
import com.visenze.productsearch.response.Product;
import com.visenze.visearch.ProductType;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <h1> Product Search Response Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 21 Jan 2021
 */
public class ProductSearchResponseTest {
    final ObjectMapper mapper = new ObjectMapper();
    final String JSON_STRING_FIELDS = "{\"reqid\":\"123REQ\",\"status\":\"OK\",\"im_id\":\"456IMAGE.jpg\"}";
    final String JSON_INT_FIELDS = "{\"page\":1,\"limit\":10,\"total\":100}";
    final String JSON_LIST_FIELDS = "{\"product_types\":[{\"type\":\"top\",\"box\":[1,2,3,4]},{\"type\":\"bottom\",\"box\":[5,6,7,8]}],\"result\":[{\"product_id\":\"RESULT_PRODUCT_ID_1\"},{\"product_id\":\"RESULT_PRODUCT_ID_2\"}]}";
    final String JSON_MAP_FIELDS = "{\"catalog_fields_mapping\":{\"product_id\":\"sku\",\"title\":\"product_name\"}}";
    final String JSON_OTHER_FIELDS = "{\"error\":{\"code\": 100,\"message\":\"Parameter required\"}}";

    /**
     * Combine two json format strings linearly
     *
     * @param first string that acts as head
     * @param second string that joins the first at the back
     * @return simple concat-ed string
     */
    private String concatJsonString(String first, String second) {
        if (first.endsWith("}") && second.startsWith("{")) {
            return first.substring(0, first.length() - 1) + "," + second.substring(1);
        }
        return null;
    }

    /**
     * Verifies string member fields - referencing JSON_STRING_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    public void verifyStringFields(ProductSearchResponse response) {
        assertEquals("123REQ",response.getRequestId());
        assertEquals("OK",response.getStatus());
        assertEquals("456IMAGE.jpg",response.getImageId());
    }

    /**
     * Verifies int member fields - referencing JSON_INT_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    public void verifyIntFields(ProductSearchResponse response) {
        assertEquals(1,response.getPage());
        assertEquals(10,response.getLimit());
        assertEquals(100,response.getTotal());
    }

    /**
     * Verifies List<T> member fields - referencing JSON_LIST_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    public void verifyListFields(ProductSearchResponse response) {
        List<ProductType> types = response.getProductTypes();
        assertEquals(2, types.size());
        assertEquals("top", types.get(0).getType());
        assertEquals(1, types.get(0).getBox().get(0).intValue());
        assertEquals(2, types.get(0).getBox().get(1).intValue());
        assertEquals(3, types.get(0).getBox().get(2).intValue());
        assertEquals(4, types.get(0).getBox().get(3).intValue());
        assertEquals("bottom", types.get(1).getType());
        assertEquals(5, types.get(1).getBox().get(0).intValue());
        assertEquals(6, types.get(1).getBox().get(1).intValue());
        assertEquals(7, types.get(1).getBox().get(2).intValue());
        assertEquals(8, types.get(1).getBox().get(3).intValue());

        List<Product> info = response.getResult();
        assertEquals(2, info.size());
        assertEquals("RESULT_PRODUCT_ID_1", info.get(0).getProductId());
        assertEquals("RESULT_PRODUCT_ID_2", info.get(1).getProductId());
    }

    /**
     * Verifies Map<T1,T2> member fields - referencing JSON_MAP_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    public void verifyMapFields(ProductSearchResponse response) {
        assertEquals("sku", response.getCatalogFieldsMapping().get("product_id"));
        assertEquals("product_name", response.getCatalogFieldsMapping().get("title"));
    }

    /**
     * Verifies any other type of member fields - referencing JSON_OTHER_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    public void verifyOtherFields(ProductSearchResponse response) {
        assertEquals(100, response.getError().getCode().intValue());
        assertEquals("Parameter required", response.getError().getMessage());
    }

    @Test
    public void testJsonDeserializeString() {
        ViSearchHttpResponse mockRawResponse = mock(ViSearchHttpResponse.class);
        ProductSearchResponse mockResponse = mock(ProductSearchResponse.class);
        SearchByImageParam mockParams = mock(SearchByImageParam.class);
        ProductSearch mockSdk = mock(ProductSearch.class);


        when(mockSdk.imageSearch(mockParams)).thenReturn(mockResponse);

        try {
            ProductSearchResponse response = mapper.readValue(JSON_STRING_FIELDS, ProductSearchResponse.class);
            verifyStringFields(response);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }

    @Test
    public void testJsonDeserializeInteger() {
        try {
            ProductSearchResponse response = mapper.readValue(JSON_INT_FIELDS, ProductSearchResponse.class);
            verifyIntFields(response);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }

    @Test
    public void testJsonDeserializeList() {
        try {
            ProductSearchResponse response = mapper.readValue(JSON_LIST_FIELDS, ProductSearchResponse.class);
            verifyListFields(response);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }

    @Test
    public void testJsonDeserializeMap() {
        try {
            ProductSearchResponse response = mapper.readValue(JSON_MAP_FIELDS, ProductSearchResponse.class);
            verifyMapFields(response);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }

    @Test
    public void testJsonDeserializeOther() {
        try {
            ProductSearchResponse response = mapper.readValue(JSON_OTHER_FIELDS, ProductSearchResponse.class);
            verifyOtherFields(response);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }

    @Test
    public void testSimulatedResponse() {
        try {
            String stringResponse = concatJsonString(JSON_STRING_FIELDS, JSON_INT_FIELDS);
            if (stringResponse == null)
                fail("Bad Json string format");

            stringResponse = concatJsonString(stringResponse, JSON_LIST_FIELDS);
            if (stringResponse == null)
                fail("Bad Json string format");

            stringResponse = concatJsonString(stringResponse, JSON_MAP_FIELDS);
            if (stringResponse == null)
                fail("Bad Json string format");

            stringResponse = concatJsonString(stringResponse, JSON_OTHER_FIELDS);
            if (stringResponse == null)
                fail("Bad Json string format");

            ProductSearchResponse response = mapper.readValue(stringResponse, ProductSearchResponse.class);
            verifyStringFields(response);
            verifyIntFields(response);
            verifyListFields(response);
            verifyMapFields(response);
            verifyOtherFields(response);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }
}