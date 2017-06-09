package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.SearchOperations;
import com.visenze.visearch.internal.SearchOperationsImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import com.visenze.visearch.internal.json.ViSearchModule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ViSearchSearchOperationsTest {

    private ViSearchHttpClient mockClient;
    private ObjectMapper objectMapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void beforeTest() {
        mockClient = mock(ViSearchHttpClient.class);
        objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
    }

    @Test
    public void testSearchParamsBasic() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        Map<String, String> responseHeaders = Maps.newHashMap();
        responseHeaders.put("test-param", "123");
        when(response.getHeaders()).thenReturn(responseHeaders);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedSearchResult = searchOperations.search(searchParams);
        assertEquals(responseHeaders, pagedSearchResult.getHeaders());
        assertEquals("test_im", searchParams.getImName());
        assertEquals(null, pagedSearchResult.getErrorMessage());
        assertEquals(null, pagedSearchResult.getCause());
        assertEquals(null, pagedSearchResult.getRawResponseMessage());
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name", "test_im");
        verify(mockClient).get("/search", expectedParams);
    }

    @Test
    public void testSearchParamsFacet() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}],\"facets\":[{\"key\":\"brand\",\"items\":[{\"value\":\"brandA\",\"count\":5},{\"value\":\"brandB\",\"count\":6},{\"value\":\"brandC\",\"count\":9}]}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im")
                .setFacets(Lists.newArrayList("brand"))
                .setFacetsLimit(10)
                .setFacetsShowCount(true);
        searchOperations.search(searchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name", "test_im");
        expectedParams.put("facets", "brand");
        expectedParams.put("facets_limit", "10");
        expectedParams.put("facets_show_count", "true");
        verify(mockClient).get("/search", expectedParams);
    }

    @Test
    public void testSearchParamsFacetOnNumber() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}],\"facets\":[{\"key\":\"brand\",\"range\":{\"min\":1, \"max\":420}}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im")
                .setFacets(Lists.newArrayList("brand"))
                .setFacetsLimit(10)
                .setFacetsShowCount(true);
        PagedSearchResult searchResult = searchOperations.search(searchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name", "test_im");
        expectedParams.put("facets", "brand");
        expectedParams.put("facets_limit", "10");
        expectedParams.put("facets_show_count", "true");
        verify(mockClient).get("/search", expectedParams);
        List<Facet> facets = searchResult.getFacets();
        assertEquals(1, facets.size());
        assertEquals(1, facets.get(0).getRange().getMin());
        assertEquals(420, facets.get(0).getRange().getMax());
    }

    @Test
    public void testSearchParamsFull() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":10,\"limit\":1,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        Map<String, String> fq = Maps.newHashMap();
        fq.put("field_a", "value_a");
        fq.put("field_b", "value_b");
        Map<String, String> custom = Maps.newHashMap();
        custom.put("custom_key", "custom_value");
        SearchParams searchParams = new SearchParams("test_im")
                .setPage(10)
                .setLimit(1)
                .setScore(true)
                .setScoreMin(0.1f)
                .setScoreMax(0.75f)
                .setFq(fq)
                .setFl(Lists.newArrayList("field_x", "field_y"))
                .setGetAllFl(true)
                .setQInfo(true)
                .setCustom(custom);
        searchOperations.search(searchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name", "test_im");
        expectedParams.put("page", "10");
        expectedParams.put("limit", "1");
        expectedParams.put("score", "true");
        expectedParams.put("score_min", "0.1");
        expectedParams.put("score_max", "0.75");
        expectedParams.put("fq", "field_a:value_a");
        expectedParams.put("fq", "field_b:value_b");
        expectedParams.put("fl", "field_x");
        expectedParams.put("fl", "field_y");
        expectedParams.put("get_all_fl", "true");
        expectedParams.put("qinfo", "true");
        expectedParams.put("custom_key", "custom_value");
        verify(mockClient).get("/search", expectedParams);
    }

    @Test
    public void testSearchResponseBasic() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"},{\"im_name\":\"test_im_1\"},{\"im_name\":\"test_im_2\"},{\"im_name\":\"test_im_3\"},{\"im_name\":\"test_im_4\"},{\"im_name\":\"test_im_5\"},{\"im_name\":\"test_im_6\"},{\"im_name\":\"test_im_7\"},{\"im_name\":\"test_im_8\"},{\"im_name\":\"test_im_9\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(new Integer(1), pagedResult.getPage());
        assertEquals(new Integer(10), pagedResult.getLimit());
        assertEquals(new Integer(20), pagedResult.getTotal());
        List<ImageResult> results = pagedResult.getResult();
        assertEquals(10, results.size());
        for (int i = 0; i < 10; i++) {
            ImageResult image = results.get(i);
            assertEquals("test_im_" + i, image.getImName());
        }
    }

    @Test
    public void testSearchResponseFacet() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":1,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"}],\"facets\":[{\"key\":\"brand\",\"items\":[{\"value\":\"brandA\",\"count\":5},{\"value\":\"brandB\",\"count\":6},{\"value\":\"brandC\",\"count\":9}]}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im")
                .setFacets(Lists.newArrayList("brand"));
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(new Integer(1), pagedResult.getPage());
        assertEquals(new Integer(1), pagedResult.getLimit());
        assertEquals(new Integer(20), pagedResult.getTotal());
        List<ImageResult> results = pagedResult.getResult();
        assertEquals(1, results.size());
        List<Facet> facetList = pagedResult.getFacets();
        assertEquals(1, facetList.size());
        Facet facet = facetList.get(0);
        assertEquals("brand", facet.getKey());
        List<FacetItem> facetItemList = facet.getFacetItems();
        assertEquals(3, facetItemList.size());
        FacetItem facetItem0 = facetItemList.get(0);
        assertEquals("brandA", facetItem0.getValue());
        assertEquals(new Integer(5), facetItem0.getCount());
        FacetItem facetItem1 = facetItemList.get(1);
        assertEquals("brandB", facetItem1.getValue());
        assertEquals(new Integer(6), facetItem1.getCount());
        FacetItem facetItem2 = facetItemList.get(2);
        assertEquals("brandC", facetItem2.getValue());
        assertEquals(new Integer(9), facetItem2.getCount());
    }

    @Test
    public void testSearchResponseFull() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":10,\"limit\":1,\"total\":20,\"qinfo\":{\"im_url\":\"http://www.example.com/test_im.jpeg\",\"price\":\"49.99\",\"title\":\"java sdk\"},\"result\":[{\"im_name\":\"test_im_0\",\"score\":0.43719249963760376,\"value_map\":{\"price\":\"67.500000\",\"title\":\"sdk test\"}}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(responseBody, pagedResult.getRawJson());
        assertEquals(new Integer(10), pagedResult.getPage());
        assertEquals(new Integer(1), pagedResult.getLimit());
        assertEquals(new Integer(20), pagedResult.getTotal());
        Map<String, String> qinfo = Maps.newHashMap();
        qinfo.put("im_url", "http://www.example.com/test_im.jpeg");
        qinfo.put("price", "49.99");
        qinfo.put("title", "java sdk");
        assertEquals(qinfo, pagedResult.getQueryInfo());
        List<ImageResult> results = pagedResult.getResult();
        ImageResult image = results.get(0);
        assertEquals("test_im_0", image.getImName());
        assertEquals(new Float(0.43719249963760376), image.getScore());
        Map<String, String> metadata = Maps.newHashMap();
        metadata.put("price", "67.500000");
        metadata.put("title", "sdk test");
        assertEquals(metadata, image.getMetadata());
    }

    @Test
    public void testSearchResponseError() {
        String responseBody = "{\"status\":\"fail\",\"method\":\"search\",\"error\":[\"Error message.\"],\"page\":1,\"limit\":10,\"total\":0}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals("Error message.", pagedResult.getErrorMessage());
        assertEquals(null, pagedResult.getCause());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testSearchResponseUnknownError() {
        String responseBody = "{\"status\":\"fail\",\"method\":\"search\",\"page\":1,\"limit\":10,\"total\":0}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), pagedResult.getErrorMessage());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testSearchResponseErrorGetJson() {
        String responseBody = "{\"status\":\"fail\",\"method\":\"search\",\"error\":[\"Error message.\"],\"page\":1,\"limit\":10,\"total\":0}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals("Error message.", pagedResult.getErrorMessage());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testSearchResponseMalformed0() {
        String responseBody = "{\"status\":\"OK\" \"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"},{\"im_name\":\"test_im_1\"},{\"im_name\":\"test_im_2\"},{\"im_name\":\"test_im_3\"},{\"im_name\":\"test_im_4\"},{\"im_name\":\"test_im_5\"},{\"im_name\":\"test_im_6\"},{\"im_name\":\"test_im_7\"},{\"im_name\":\"test_im_8\"},{\"im_name\":\"test_im_9\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), pagedResult.getErrorMessage());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testSearchResponseMalformed1() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":{}}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), pagedResult.getErrorMessage());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testSearchResponseMalformed2() {
        String responseBody = "{\"status\":\"OK\",\"error\":[],\"total\":20,\"result\":[]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), pagedResult.getErrorMessage());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testSearchResponseMalformed3() throws Exception {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":10,\"limit\":1,\"total\":20,\"qinfo\":[\"im_url\",\"price\",\"title\"],\"result\":[]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), pagedResult.getErrorMessage());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testSearchResponseMalformed4() {
        String responseBody = "{\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":0,\"result\":[]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), pagedResult.getErrorMessage());
        assertEquals(responseBody, pagedResult.getRawResponseMessage());
    }

    @Test
    public void testColorSearchParams() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"colorsearch\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        ColorSearchParams colorSearchParams = new ColorSearchParams("123ABC");
        assertEquals("123ABC", colorSearchParams.getColor());
        searchOperations.colorSearch(colorSearchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("color", "123ABC");
        verify(mockClient).get("/colorsearch", expectedParams);
    }

    @Test
    public void testColorSearchJsonFormatError() {
        String responseBody = "{\"status\":\"OK\" \"method\":\"colorsearch\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        ColorSearchParams colorSearchParams = new ColorSearchParams("123ABC");
        assertEquals("123ABC", colorSearchParams.getColor());
        PagedSearchResult searchResult = searchOperations.colorSearch(colorSearchParams);
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), searchResult.getErrorMessage());
        assertEquals(responseBody, searchResult.getRawResponseMessage());
    }

    @Test
    public void testColorSearchParamsInvalid() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid color. " +
                "It should be a six hexadecimal number color code e.g. 123ACF.");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        ColorSearchParams colorSearchParams = new ColorSearchParams("#123ABC");
    }

    @Test
    public void testUploadSearchParamsURL() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"upload\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        UploadSearchParams uploadSearchParams = new UploadSearchParams("http://www.example.com/test_im.jpeg");
        assertEquals("http://www.example.com/test_im.jpeg", uploadSearchParams.getImageUrl());
        searchOperations.uploadSearch(uploadSearchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_url", "http://www.example.com/test_im.jpeg");
        verify(mockClient).post("/uploadsearch", expectedParams);
    }

    @Test
    public void testUploadSearchParamsURLWithDetection() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"uploadsearch\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"product_types\":[{\"type\":\"top\",\"score\":1,\"box\":[84,223,425,639]},{\"type\":\"shoe\",\"score\":0.24432674050331116,\"box\":[522,77,865,337]},{\"type\":\"bag\",\"score\":0.2392924576997757,\"box\":[538,437,684,694]}],\"product_types_list\":[{\"type\":\"bag\"},{\"type\":\"bottom\"},{\"type\":\"dress\"},{\"type\":\"shoe\"},{\"type\":\"top\"},{\"type\":\"other\"}],\"result\":[],\"im_id\":\"abc.png\"}\n";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        UploadSearchParams uploadSearchParams = new UploadSearchParams("http://www.example.com/test_im.jpeg");
        uploadSearchParams.setDetection("dress");
        assertEquals("http://www.example.com/test_im.jpeg", uploadSearchParams.getImageUrl());
        PagedSearchResult uploadSearchResult = searchOperations.uploadSearch(uploadSearchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_url", "http://www.example.com/test_im.jpeg");
        expectedParams.put("detection", "dress");
        verify(mockClient).post("/uploadsearch", expectedParams);
        assertEquals(3, uploadSearchResult.getProductTypes().size());
        assertEquals(6, uploadSearchResult.getProductTypesList().size());
        for (ProductType productType : uploadSearchResult.getProductTypes()) {
            assertNotNull(productType.getType());
            assertNotNull(productType.getScore());
            assertNotNull(productType.getBox());
            assertEquals(4, productType.getBox().size());
        }
        for (ProductType productType : uploadSearchResult.getProductTypesList()) {
            assertNotNull(productType.getType());
            assertNull(productType.getScore());
            assertNull(productType.getBox());
        }

        assertEquals("abc.png", uploadSearchResult.getImId());
    }

    @Test
    public void testUploadSearchParamsNullFile() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("The image file must not be null");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        File nullFile = null;
        UploadSearchParams uploadSearchParams = new UploadSearchParams(nullFile);
        searchOperations.uploadSearch(uploadSearchParams);
    }

    @Test
    public void testUploadSearchParamsNonFile() {
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        File nonFile = new File("nonFile");
        UploadSearchParams uploadSearchParams = new UploadSearchParams(nonFile);
        PagedSearchResult response = searchOperations.uploadSearch(uploadSearchParams);
        assertEquals(ResponseMessages.INVALID_IMAGE_OR_URL.getMessage(), response.getErrorMessage());
    }

    @Test
    public void testUploadSearchParamsNullStream() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("The image input stream must not be null");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        InputStream inputStream = null;
        UploadSearchParams uploadSearchParams = new UploadSearchParams(inputStream);
        searchOperations.uploadSearch(uploadSearchParams);
    }

    @Test
    public void testDiscoverSearchParamsURL() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"similarproducts\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"qinfo\":{\"im_url\":\"http://www.example.com/test_im.jpeg\",\"price\":\"49.99\",\"title\":\"java sdk\"},\"objects\":[{\"type\":\"top\",\"score\":1,\"box\":[84,223,425,639],\"result\":[{\"im_name\":\"test_im_0\",\"score\":0.43719249963760376,\"value_map\":{\"price\":\"67.500000\",\"title\":\"sdk test\"}}]},{\"type\":\"shoe\",\"score\":0.24432674050331116,\"box\":[522,77,865,337],\"result\":[{\"im_name\":\"test_im_0\",\"score\":0.43719249963760376,\"value_map\":{\"price\":\"67.500000\",\"title\":\"sdk test\"}}]},{\"type\":\"bag\",\"score\":0.2392924576997757,\"box\":[538,437,684,694],\"result\":[{\"im_name\":\"test_im_0\",\"score\":0.43719249963760376,\"value_map\":{\"price\":\"67.500000\",\"title\":\"sdk test\"}}]}],\"object_types_list\":[{\"type\":\"bag\"},{\"type\":\"bottom\"},{\"type\":\"dress\"},{\"type\":\"shoe\"},{\"type\":\"top\"},{\"type\":\"other\"}],\"im_id\":\"abc.png\"}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        UploadSearchParams uploadSearchParams = new UploadSearchParams("http://www.example.com/test_im.jpeg");
        assertEquals("http://www.example.com/test_im.jpeg", uploadSearchParams.getImageUrl());
        PagedSearchResult uploadSearchResult = searchOperations.discoverSearch(uploadSearchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_url", "http://www.example.com/test_im.jpeg");
        verify(mockClient).post("/discoversearch", expectedParams);
        assertEquals(3, uploadSearchResult.getObjects().size());
        assertEquals(6, uploadSearchResult.getObjectTypesList().size());
        for (ObjectSearchResult object : uploadSearchResult.getObjects()) {
            assertNotNull(object.getType());
            assertNotNull(object.getScore());
            assertNotNull(object.getBox());
            assertEquals(4, object.getBox().size());
        }
        for (ProductType productType : uploadSearchResult.getObjectTypesList()) {
            assertNotNull(productType.getType());
            assertNull(productType.getScore());
            assertNull(productType.getBox());
        }
        assertEquals(responseBody, uploadSearchResult.getRawJson());
        assertEquals(new Integer(1), uploadSearchResult.getPage());
        assertEquals(new Integer(10), uploadSearchResult.getLimit());
        assertEquals(new Integer(20), uploadSearchResult.getTotal());
        List<ObjectSearchResult> objects = uploadSearchResult.getObjects();
        assertEquals(3, objects.size());

        Map<String, String> qinfo = Maps.newHashMap();
        qinfo.put("im_url", "http://www.example.com/test_im.jpeg");
        qinfo.put("price", "49.99");
        qinfo.put("title", "java sdk");
        assertEquals(qinfo, uploadSearchResult.getQueryInfo());

        assertEquals("abc.png", uploadSearchResult.getImId());

        ImageResult image = objects.get(0).getResult().get(0);
        assertEquals("test_im_0", image.getImName());
        assertEquals(new Float(0.43719249963760376), image.getScore());
        Map<String, String> metadata = Maps.newHashMap();
        metadata.put("price", "67.500000");
        metadata.put("title", "sdk test");
        assertEquals(metadata, image.getMetadata());
    }

    @Test
    public void testDiscoverSearchParamsNullFile() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("The image file must not be null");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        File nullFile = null;
        UploadSearchParams uploadSearchParams = new UploadSearchParams(nullFile);
        searchOperations.discoverSearch(uploadSearchParams);
    }

    @Test
    public void testDiscoverSearchParamsNonFile() {
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        File nonFile = new File("nonFile");
        UploadSearchParams uploadSearchParams = new UploadSearchParams(nonFile);
        PagedSearchResult response = searchOperations.discoverSearch(uploadSearchParams);
        assertEquals(ResponseMessages.INVALID_IMAGE_OR_URL.getMessage(), response.getErrorMessage());
    }

    @Test
    public void testSimilarProductsSearchParamsNullStream() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("The image input stream must not be null");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        InputStream inputStream = null;
        UploadSearchParams uploadSearchParams = new UploadSearchParams(inputStream);
        searchOperations.discoverSearch(uploadSearchParams);
    }

    // should not throw anything
    @Test
    public void testUploadSearchParamsImId() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"},{\"im_name\":\"test_im_1\"},{\"im_name\":\"test_im_2\"},{\"im_name\":\"test_im_3\"},{\"im_name\":\"test_im_4\"},{\"im_name\":\"test_im_5\"},{\"im_name\":\"test_im_6\"},{\"im_name\":\"test_im_7\"},{\"im_name\":\"test_im_8\"},{\"im_name\":\"test_im_9\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);

        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        UploadSearchParams uploadSearchParams = new UploadSearchParams();
        uploadSearchParams.setImId("abc");
        PagedSearchResult sr = searchOperations.uploadSearch(uploadSearchParams);

        assertEquals(null, sr.getErrorMessage());

    }
}
