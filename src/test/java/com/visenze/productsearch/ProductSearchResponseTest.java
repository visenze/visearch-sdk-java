package com.visenze.productsearch;

import com.google.common.collect.Multimap;
import com.visenze.productsearch.http.ProductSearchHttpClientImpl;
import com.visenze.productsearch.param.SearchByIdParam;
import com.visenze.productsearch.param.SearchByImageParam;
import com.visenze.productsearch.response.Experiment;
import com.visenze.productsearch.response.GroupProductResult;
import com.visenze.productsearch.response.Product;
import com.visenze.visearch.ProductType;
import com.visenze.visearch.SetInfo;
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
        assertEquals(groupProductResults.size(), 1);

        GroupProductResult g1 = groupProductResults.get(0);
        assertEquals("Clothing/Shirts", g1.getGroupByValue());
        assertEquals("pid1", g1.getProducts().get(0).getProductId());
        assertEquals("Sustainable Cotton The New Later Graphic Tee White", g1.getProducts().get(0).getData().get("product_name").asString());


    }

    @Test
    public void testRecommendationResponse() {
        String json = "{\n" +
                "    \"reqid\": \"017a3bb0a050fb56218beb28b9e5ec\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"product/recommendations\",\n" +
                "    \"page\": 1,\n" +
                "    \"limit\": 10,\n" +
                "    \"total\": 2,\n" +
                "    \"product_types\": [],\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"product_id\": \"top-name-1\",\n" +
                "            \"main_image_url\": \"https://localhost/top-name-1.jpg\",\n" +
                "            \"data\": {\n" +
                "                \"title\": \"top-name-001\"\n" +
                "            },\n" +
                "            \"tags\": {\n" +
                "                \"category\": \"top\"\n" +
                "            },\n" +
                "            \"alternatives\": [\n" +
                "                {\n" +
                "                    \"product_id\": \"top-name-2\",\n" +
                "                    \"main_image_url\": \"https://localhost/top-name-2.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"top-name-002\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"product_id\": \"top-name-3\",\n" +
                "                    \"main_image_url\": \"https://localhost/top-name-3.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"top-name-003\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"pants-name-1\",\n" +
                "            \"main_image_url\": \"https://localhost/pants-name-1.jpg\",\n" +
                "            \"data\": {\n" +
                "                \"title\": \"pants-name-001\"\n" +
                "            },\n" +
                "            \"tags\": {\n" +
                "                \"category\": \"pants\"\n" +
                "            },\n" +
                "            \"alternatives\": [\n" +
                "                {\n" +
                "                    \"product_id\": \"pants-name-2\",\n" +
                "                    \"main_image_url\": \"https://localhost/pants-name-2.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"pants-name-002\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"strategy\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Visually similar\",\n" +
                "        \"algorithm\": \"VSR\"\n" +
                "    },\n" +
                "    \"alt_limit\": 5\n" +
                "}";

        ProductSearchResponse response = GetRecommendationMockedResponse(json);
        assertEquals(1, response.getPage());
        assertEquals(10, response.getLimit());
        assertEquals(2, response.getTotal());
        assertEquals(5, response.getAltLimit().intValue());
        assertEquals(1, response.getStrategy().getId().intValue());
        assertEquals("Visually similar", response.getStrategy().getName());
        assertEquals("VSR", response.getStrategy().getAlgorithm());
        assertEquals(2, response.getResult().size());

        assertEquals("top-name-1", response.getResult().get(0).getProductId());
        assertNull(response.getResult().get(0).getPinned());
        assertNull(response.getExcludedPids());
        assertEquals("top", response.getResult().get(0).getTags().get("category").asString());
        assertEquals("https://localhost/top-name-1.jpg", response.getResult().get(0).getMainImageUrl());
        assertEquals("top-name-001", response.getResult().get(0).getData().get("title").asString());
        assertEquals(2, response.getResult().get(0).getAlternatives().size());
        assertEquals("top-name-2", response.getResult().get(0).getAlternatives().get(0).getProductId());
        assertEquals("https://localhost/top-name-2.jpg", response.getResult().get(0).getAlternatives().get(0).getMainImageUrl());
        assertEquals("top-name-002", response.getResult().get(0).getAlternatives().get(0).getData().get("title").asString());
        assertEquals("top-name-3", response.getResult().get(0).getAlternatives().get(1).getProductId());
        assertEquals("https://localhost/top-name-3.jpg", response.getResult().get(0).getAlternatives().get(1).getMainImageUrl());
        assertEquals("top-name-003", response.getResult().get(0).getAlternatives().get(1).getData().get("title").asString());

        assertEquals("pants-name-1", response.getResult().get(1).getProductId());
        assertEquals("pants", response.getResult().get(1).getTags().get("category").asString());
        assertEquals("https://localhost/pants-name-1.jpg", response.getResult().get(1).getMainImageUrl());
        assertEquals("pants-name-001", response.getResult().get(1).getData().get("title").asString());
        assertEquals(1, response.getResult().get(1).getAlternatives().size());
        assertEquals("pants-name-2", response.getResult().get(1).getAlternatives().get(0).getProductId());
        assertEquals("https://localhost/pants-name-2.jpg", response.getResult().get(1).getAlternatives().get(0).getMainImageUrl());
        assertEquals("pants-name-002", response.getResult().get(1).getAlternatives().get(0).getData().get("title").asString());
        assertNull(response.getExperiment());
    }

    @Test
    public void testRecommendationExperimentResponse() {
        String json = "{\n" +
                "    \"reqid\": \"01806a667776c6f8a31c28105fd99b\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"product/recommendations\",\n" +
                "    \"page\": 1,\n" +
                "    \"limit\": 10,\n" +
                "    \"total\": 2,\n" +
                "    \"product_types\": [],\n" +
                "    \"result\": [\n" +
                "    ],\n" +
                "    \"strategy\": {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"test\",\n" +
                "        \"algorithm\": \"VSR\"\n" +
                "    },\n" +
                "   \"experiment\": {\n" +
                "        \"experiment_id\": 522,\n" +
                "        \"variant_id\": 2019,\n" +
                "        \"strategy_id\": 3,\n" +
                "        \"experiment_no_recommendation\": false,\n" +
                "        \"debug\": {\n" +
                "            \"experimentDebugLogs\": [\n" +
                "                {\n" +
                "                    \"experimentID\": 522,\n" +
                "                    \"msg\": \"matched all constraints. rollout yes. {BucketNum:260 DistributionArray:{VariantIDs:[2019 2020] PercentsAccumulated:[500 1000]} VariantID:2019 RolloutPercent:100}\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }," +
                "    \"alt_limit\": 5\n" +
                "}";


        ProductSearchResponse response = GetRecommendationMockedResponse(json);
        Experiment experiment = response.getExperiment();
        assertEquals(522, experiment.getExperimentId());
        assertEquals(2019, experiment.getVariantId());
        assertFalse(experiment.isExpNoRecommendation());
        assertTrue(3 == experiment.getStrategyId());
        assertNotNull(experiment.getDebug());
    }

    @Test
    public void testRecommendationPinExcludedResponse() {
        String json = "{\n" +
                "    \"reqid\": \"01806a667776c6f8a31c28105fd99e\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"product/recommendations\",\n" +
                "    \"page\": 1,\n" +
                "    \"limit\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"product_types\": [],\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"product_id\": \"top-name-11\",\n" +
                "            \"main_image_url\": \"https://localhost/top-name-11.jpg\",\n" +
                "            \"data\": {\n" +
                "                \"title\": \"top-name-001\"\n" +
                "            },\n" +
                "            \"tags\": {\n" +
                "                \"category\": \"top\"\n" +
                "            },\n" +
                "            \"pinned\": \"true\",\n" +
                "            \"alternatives\": [\n" +
                "                {\n" +
                "                    \"product_id\": \"top-name-22\",\n" +
                "                    \"main_image_url\": \"https://localhost/top-name-22.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"top-name-002\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"product_id\": \"top-name-33\",\n" +
                "                    \"main_image_url\": \"https://localhost/top-name-33.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"top-name-003\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"strategy\": {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"test\",\n" +
                "        \"algorithm\": \"STL\"\n" +
                "    },\n" +
                "    \"excluded_pids\" : [\"p1\", \"p2\"],\n" +
                "    \"alt_limit\": 5\n" +
                "}";


        ProductSearchResponse response = GetRecommendationMockedResponse(json);
        assertTrue(response.getResult().get(0).getPinned());
        assertEquals(2, response.getExcludedPids().size());
        assertEquals("p1" , response.getExcludedPids().get(0));
        assertEquals("p2" , response.getExcludedPids().get(1));

        json = json.replace("\"pinned\": \"true\"","\"pinned\": \"false\"");
        response = GetRecommendationMockedResponse(json);
        assertFalse(response.getResult().get(0).getPinned());

    }

    @Test
    public void testRecommendationCtlSetResponse() {
        String json = "{\n" +
                "    \"reqid\": \"01806a667776c6f8a31c28105fd99e\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"product/recommendations\",\n" +
                "    \"page\": 1,\n" +
                "    \"limit\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"product_types\": [],\n" +
                "    \"result\": [\n" +
                        "{\n" +
                        "      \"product_id\": \"dress1\",\n" +
                        "      \"main_image_url\": \"http://test.com/img1.jpg\",\n" +
                        "      \"tags\": {\n" +
                        "        \"category\": \"dress\",\n" +
                        "        \"set_id\": \"set1\"\n" +
                        "      },\n" +
                        "      \"score\": 0.9\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"product_id\": \"shirt1\",\n" +
                        "      \"main_image_url\": \"http://test.com/img2.jpg\",\n" +
                        "      \"tags\": {\n" +
                        "        \"category\": \"shirt\",\n" +
                        "        \"set_id\": \"set1\"\n" +
                        "      },\n" +
                        "      \"score\": 0.7\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"product_id\": \"shoe2\",\n" +
                        "      \"main_image_url\": \"http://test.com/img2.jpg\",\n" +
                        "      \"tags\": {\n" +
                        "        \"category\": \"shoes\",\n" +
                        "        \"set_id\": \"set2\"\n" +
                        "      },\n" +
                        "      \"score\": 0.8\n" +
                        "    }" +
                "    ],\n" +
                    "\"set_info\": [\n" +
                    "    {\n" +
                    "      \"set_id\": \"set1\",\n" +
                    "      \"set_score\": 1000\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"set_id\": \"set2\",\n" +
                    "      \"set_score\": 900\n" +
                    "    }\n" +
                    "  ]," +
                "    \"strategy\": {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"test\",\n" +
                "        \"algorithm\": \"CTL\"\n" +
                "    }\n" +
                "}";


        ProductSearchResponse response = GetRecommendationMockedResponse(json);
        List<Product> result = response.getResult();
        assertEquals(3, result.size());

        assertEquals("set1", result.get(0).getTags().get("set_id").asString());
        assertEquals("dress", result.get(0).getTags().get("category").asString());

        assertEquals("set1", result.get(1).getTags().get("set_id").asString());
        assertEquals("shirt", result.get(1).getTags().get("category").asString());

        assertEquals("set2", result.get(2).getTags().get("set_id").asString());
        assertEquals("shoes", result.get(2).getTags().get("category").asString());

        List<SetInfo> setInfoList = response.getSetInfoList();
        assertEquals(2, setInfoList.size());

        assertEquals("set1", setInfoList.get(0).getSetId());
        assertTrue(1000 == setInfoList.get(0).getSetScore());

        assertEquals("set2", setInfoList.get(1).getSetId());
        assertTrue(900 == setInfoList.get(1).getSetScore());

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

    /**
     * Get mocked response for recommendation api
     *
     * @param mockedBodyResponse JSON formatted response to be used as http body
     * @return Mock response parsed
     */
    private ProductSearchResponse GetRecommendationMockedResponse(String mockedBodyResponse) {
        // create mock response to return the desired body
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(mockedBodyResponse);
        // create mock http client, to skip any actual http calls/operations,
        // just jump straight to returning mocked response
        ProductSearchHttpClientImpl mockClient = mock(ProductSearchHttpClientImpl.class);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        // set ProductSearch to use the mock client for mock responses
        SearchByIdParam dummyParams = new SearchByIdParam("dummy_product_id");
        ProductSearch sdk = new ProductSearch.Builder("dummy",0).setApiEndPoint("dummy").build();
        sdk.setHttpClient(mockClient);
        return sdk.recomendation(dummyParams);
    }
}