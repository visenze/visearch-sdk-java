package com.visenze.productsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import com.visenze.productsearch.http.ProductSearchHttpClientImpl;
import com.visenze.productsearch.param.SearchByImageParam;
import com.visenze.productsearch.response.GroupProductResult;
import com.visenze.productsearch.response.Product;
import com.visenze.visearch.ProductType;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;

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
@RunWith(MockitoJUnitRunner.class)
public class ProductSearchResponseTest {
    final ObjectMapper mapper = new ObjectMapper();
    final String JSON_STRING_FIELDS = "{\"reqid\":\"123REQ\",\"status\":\"OK\",\"im_id\":\"456IMAGE.jpg\"}";
    final String JSON_INT_FIELDS = "{\"page\":1,\"limit\":10,\"total\":100}";
    final String JSON_LIST_FIELDS = "{\"product_types\":[{\"type\":\"top\",\"score\":0.912,\"box\":[1,2,3,4]},{\"type\":\"bottom\",\"box\":[5,6,7,8],\"score\":0}],\"result\":[{\"product_id\":\"RESULT_PRODUCT_ID_1\"},{\"product_id\":\"RESULT_PRODUCT_ID_2\"}]}";
    final String JSON_MAP_FIELDS = "{\"catalog_fields_mapping\":{\"product_id\":\"sku\",\"title\":\"product_name\"}}";
    final String JSON_OTHER_FIELDS = "{\"error\":{\"code\": 100,\"message\":\"Parameter required\"}}";


    @Test
    public void testGroupResponse() {
        String json = "{\n" +
                "    \"im_id\": \"20210128365611e0ec495d7dcb4a8488bb21b9f960d430bfc59.jpg\",\n" +
                "    \"reqid\": \"017748e2c400644bd662db1d5c2eeb\",\n" +
                "    \"page\": 1,\n" +
                "    \"total\": 956,\n" +
                "    \"group_by_key\": \"merchant_category\",\n" +
                "    \"group_limit\": 1,\n" +
                "    \"group_results\": [\n" +
                "        {\n" +
                "            \"group_by_value\": \"Clothing/Shirts\",\n" +
                "            \"result\": [\n" +
                "                {\n" +
                "                    \"product_id\": \"pid1\",\n" +
                "                    \"main_image_url\": \"http://d3vhkxmeglg6u9.cloudfront.net/img/p/2/2/9/1/2/1/229121.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"product_name\": \"Sustainable Cotton The New Later Graphic Tee White\"\n" +
                "                    },\n" +
                "                    \"score\": 0.5553306\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"similar-products\",\n" +
                "    \"product_types\": [\n" +
                "        {\n" +
                "            \"type\": \"top\",\n" +
                "            \"score\": 0.912,\n" +
                "            \"box\": [\n" +
                "                132,\n" +
                "                285,\n" +
                "                682,\n" +
                "                931\n" +
                "            ],\n" +
                "            \"attributes\": {}\n" +
                "        }\n" +
                "    ],\n" +
                "    \"result\": []\n" +
                "}";

        ProductSearchResponse response = GetMockedResponse(json);

        List<GroupProductResult> groupProductResults = response.getGroupProductResults();
        assertTrue(groupProductResults.size() == 1);

        GroupProductResult g1 = groupProductResults.get(0);
        assertEquals("Clothing/Shirts", g1.getGroupByValue());
        assertEquals("pid1", g1.getProducts().get(0).getProductId());
        assertEquals("Sustainable Cotton The New Later Graphic Tee White", g1.getProducts().get(0).getData().get("product_name").asString());


    }

    @Test
    public void testJsonDeserializeString() {
        verifyStringFields(GetMockedResponse(JSON_STRING_FIELDS));
    }

    @Test
    public void testJsonDeserializeInteger() {
        verifyIntFields(GetMockedResponse(JSON_INT_FIELDS));
    }

    @Test
    public void testJsonDeserializeList() {
        verifyListFields(GetMockedResponse(JSON_LIST_FIELDS));
    }

    @Test
    public void testJsonDeserializeMap() {
        verifyMapFields(GetMockedResponse(JSON_MAP_FIELDS));
    }

    @Test
    public void testJsonDeserializeOther() {
        verifyOtherFields(GetMockedResponse(JSON_OTHER_FIELDS));
    }

    @Test
    public void testSimulatedResponse() {
        String stringResponse = concatJsonString(JSON_STRING_FIELDS, JSON_INT_FIELDS);
        stringResponse = concatJsonString(stringResponse, JSON_LIST_FIELDS);
        stringResponse = concatJsonString(stringResponse, JSON_MAP_FIELDS);
        stringResponse = concatJsonString(stringResponse, JSON_OTHER_FIELDS);

        ProductSearchResponse response = GetMockedResponse(stringResponse);
        verifyStringFields(response);
        verifyIntFields(response);
        verifyListFields(response);
        verifyMapFields(response);
        verifyOtherFields(response);
    }

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
        fail();
        return first;
    }

    /**
     * Verifies string member fields - referencing JSON_STRING_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    private void verifyStringFields(ProductSearchResponse response) {
        assertEquals("123REQ",response.getRequestId());
        assertEquals("OK",response.getStatus());
        assertEquals("456IMAGE.jpg",response.getImageId());
    }

    /**
     * Verifies int member fields - referencing JSON_INT_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    private void verifyIntFields(ProductSearchResponse response) {
        assertEquals(1,response.getPage());
        assertEquals(10,response.getLimit());
        assertEquals(100,response.getTotal());
    }

    /**
     * Verifies List<T> member fields - referencing JSON_LIST_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    private void verifyListFields(ProductSearchResponse response) {
        List<ProductType> types = response.getProductTypes();
        assertEquals(2, types.size());
        assertEquals("top", types.get(0).getType());
        assertEquals(1, types.get(0).getBox().get(0).intValue());
        assertEquals(2, types.get(0).getBox().get(1).intValue());
        assertEquals(3, types.get(0).getBox().get(2).intValue());
        assertEquals(4, types.get(0).getBox().get(3).intValue());
        assertEquals(String.valueOf(types.get(0).getScore()) , "0.912");
        assertEquals("bottom", types.get(1).getType());
        assertEquals(5, types.get(1).getBox().get(0).intValue());
        assertEquals(6, types.get(1).getBox().get(1).intValue());
        assertEquals(7, types.get(1).getBox().get(2).intValue());
        assertEquals(8, types.get(1).getBox().get(3).intValue());
        assertEquals(types.get(1).getScore() , Float.valueOf(0f));

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
    private void verifyMapFields(ProductSearchResponse response) {
        assertEquals("sku", response.getCatalogFieldsMapping().get("product_id"));
        assertEquals("product_name", response.getCatalogFieldsMapping().get("title"));
    }

    /**
     * Verifies any other type of member fields - referencing JSON_OTHER_FIELDS
     *
     * @param response ProductSearchResponse to verify
     */
    private void verifyOtherFields(ProductSearchResponse response) {
        assertEquals(100, response.getError().getCode().intValue());
        assertEquals("Parameter required", response.getError().getMessage());
    }

    /**
     * Get mocked response
     *
     * @param mockedBodyResponse JSON formatted response to be used as http body
     * @return Mock response parsed
     */
    private ProductSearchResponse GetMockedResponse(String mockedBodyResponse) {
        // create mock response to return the desired body
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(mockedBodyResponse);
        // create mock http client, to skip any actual http calls/operations,
        // just jump straight to returning mocked response
        ProductSearchHttpClientImpl mockClient = mock(ProductSearchHttpClientImpl.class);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        // set ProductSearch to use the mock client for mock responses
        SearchByImageParam dummyParams = SearchByImageParam.newFromImageUrl("dummy");
        ProductSearch sdk = new ProductSearch.Builder("dummy",0).setApiEndPoint("dummy").build();
        sdk.setHttpClient(mockClient);
        return sdk.imageSearch(dummyParams);
    }
}