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
                .setFacet(true)
                .setFacetField(Lists.newArrayList("brand"));
        searchOperations.search(searchParams);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name", "test_im");
        expectedParams.put("facet", "true");
        expectedParams.put("facet_field", "brand");
        verify(mockClient).get("/search", expectedParams);
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
                .setGet_all_fl(true)
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
                .setFacet(true)
                .setFacetField(Lists.newArrayList("brand"));
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
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("An error occurred calling ViSearch: " + "Error message.");
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseUnknownError() {
        String responseBody = "{\"status\":\"fail\",\"method\":\"search\",\"page\":1,\"limit\":10,\"total\":0}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("An unknown error occurred in ViSearch: " + responseBody);
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseErrorGetJson() {
        String responseBody = "{\"status\":\"fail\",\"method\":\"search\",\"error\":[\"Error message.\"],\"page\":1,\"limit\":10,\"total\":0}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        try {
            SearchParams searchParams = new SearchParams("test_im");
            PagedSearchResult pagedResult = searchOperations.search(searchParams);
        } catch (ViSearchException e) {
            assertEquals(responseBody, e.getJson());
        }
    }

    @Test
    public void testSearchResponseMalformed0() {
        String responseBody = "{\"status\":\"OK\" \"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"},{\"im_name\":\"test_im_1\"},{\"im_name\":\"test_im_2\"},{\"im_name\":\"test_im_3\"},{\"im_name\":\"test_im_4\"},{\"im_name\":\"test_im_5\"},{\"im_name\":\"test_im_6\"},{\"im_name\":\"test_im_7\"},{\"im_name\":\"test_im_8\"},{\"im_name\":\"test_im_9\"}]}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the ViSearch response: " + responseBody);
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed1() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":{}}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the ViSearch response for list of ImageResult: {}");
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed2() {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"total\":20,\"result\":[]}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the paged ViSearch response: " + responseBody);
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed3() throws Exception {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":10,\"limit\":1,\"total\":20,\"qinfo\":[\"im_url\",\"price\",\"title\"],\"result\":[]}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the ViSearch response for map<String, String>: [\"im_url\",\"price\",\"title\"]");
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed4() {
        String responseBody = "{\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":0,\"result\":[]}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("There was a malformed ViSearch response: " + responseBody);
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
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
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Could not open the image file");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        File nonFile = new File("nonFile");
        UploadSearchParams uploadSearchParams = new UploadSearchParams(nonFile);
        searchOperations.uploadSearch(uploadSearchParams);
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
}
