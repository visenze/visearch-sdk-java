package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.twelvemonkeys.io.NullInputStream;
import com.visenze.visearch.internal.SearchOperations;
import com.visenze.visearch.internal.SearchOperationsImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.json.ViSearchModule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
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
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        searchOperations.search(searchParams);
        assertEquals("test_im", searchParams.getImName());
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name", "test_im");
        verify(mockClient).get("/search", expectedParams);
    }

    @Test
    public void testSearchParamsFacet() {
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}],\"facets\":[{\"key\":\"brand\",\"items\":[{\"value\":\"brandA\",\"count\":5},{\"value\":\"brandB\",\"count\":6},{\"value\":\"brandC\",\"count\":9}]}]}";
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
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":10,\"limit\":1,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
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
        expectedParams.put("qinfo", "true");
        expectedParams.put("custom_key", "custom_value");
        verify(mockClient).get("/search", expectedParams);
    }

    @Test
    public void testSearchResponseBasic() {
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"},{\"im_name\":\"test_im_1\"},{\"im_name\":\"test_im_2\"},{\"im_name\":\"test_im_3\"},{\"im_name\":\"test_im_4\"},{\"im_name\":\"test_im_5\"},{\"im_name\":\"test_im_6\"},{\"im_name\":\"test_im_7\"},{\"im_name\":\"test_im_8\"},{\"im_name\":\"test_im_9\"}]}";
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
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":1,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"}],\"facets\":[{\"key\":\"brand\",\"items\":[{\"value\":\"brandA\",\"count\":5},{\"value\":\"brandB\",\"count\":6},{\"value\":\"brandC\",\"count\":9}]}]}";
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
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":10,\"limit\":1,\"total\":20,\"qinfo\":{\"im_url\":\"http://www.example.com/test_im.jpeg\",\"price\":\"49.99\",\"title\":\"java sdk\"},\"result\":[{\"im_name\":\"test_im_0\",\"score\":0.43719249963760376,\"value_map\":{\"price\":\"67.500000\",\"title\":\"sdk test\"}}]}";
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
        assertEquals(response, pagedResult.getRawJson());
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
        String response = "{\"status\":\"fail\",\"method\":\"search\",\"error\":[\"Error message.\"],\"page\":1,\"limit\":10,\"total\":0}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("An error occurred calling ViSearch: " + "Error message.");
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed0() {
        String response = "{\"status\":\"OK\" \"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_0\"},{\"im_name\":\"test_im_1\"},{\"im_name\":\"test_im_2\"},{\"im_name\":\"test_im_3\"},{\"im_name\":\"test_im_4\"},{\"im_name\":\"test_im_5\"},{\"im_name\":\"test_im_6\"},{\"im_name\":\"test_im_7\"},{\"im_name\":\"test_im_8\"},{\"im_name\":\"test_im_9\"}]}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the ViSearch response: " + response);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed1() {
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":{}}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the ViSearch response for list of ImageResult: {}");
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed2() {
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"total\":20,\"result\":[]}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the paged ViSearch response: " + response);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testSearchResponseMalformed3() throws Exception {
        String response = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":10,\"limit\":1,\"total\":20,\"qinfo\":[\"im_url\",\"price\",\"title\"],\"result\":[]}";
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not parse the ViSearch response for map<String, String>: [\"im_url\",\"price\",\"title\"]");
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        SearchParams searchParams = new SearchParams("test_im");
        PagedSearchResult pagedResult = searchOperations.search(searchParams);
    }

    @Test
    public void testColorSearchParams() {
        String response = "{\"status\":\"OK\",\"method\":\"colorsearch\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
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
        String response = "{\"status\":\"OK\",\"method\":\"upload\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
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
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not found the image file");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        File nullFile = new File("/null");
        UploadSearchParams uploadSearchParams = new UploadSearchParams(nullFile);
        searchOperations.uploadSearch(uploadSearchParams);
    }

    @Test
    public void testUploadSearchParamsNullStream() {
        expectedException.expect(ViSearchException.class);
        expectedException.expectMessage("Could not read the image from input stream.");
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        UploadSearchParams uploadSearchParams = new UploadSearchParams(new NullInputStream());
        searchOperations.uploadSearch(uploadSearchParams);
    }

    @Test
    public void testUploadSearchParamsImage() {
        String response = "{\"status\":\"OK\",\"method\":\"upload\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        when(mockClient.postImage(anyString(), Matchers.<Multimap<String, String>>any(), Matchers.<byte[]>any(), anyString())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        URL resourceUrl = getClass().getResource("/1600x1600.jpeg");
        File imageFile = new File(resourceUrl.getFile());
        UploadSearchParams uploadSearchParams = new UploadSearchParams(imageFile);
        assertEquals(imageFile, uploadSearchParams.getImageFile());
        searchOperations.uploadSearch(uploadSearchParams);
        Multimap<String, String> params = HashMultimap.create();
        verify(mockClient).postImage(eq("/uploadsearch"), eq(params), Matchers.<byte[]>any(), eq(imageFile.getName()));
    }

    @Test
    public void testUploadSearchParamsImageStream() throws Exception {
        String response = "{\"status\":\"OK\",\"method\":\"upload\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        when(mockClient.postImage(anyString(), Matchers.<Multimap<String, String>>any(), Matchers.<byte[]>any(), anyString())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        URL resourceUrl = getClass().getResource("/1600x1600.jpeg");
        File imageFile = new File(resourceUrl.getFile());
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        UploadSearchParams uploadSearchParams = new UploadSearchParams(fileInputStream);
        assertEquals(fileInputStream, uploadSearchParams.getImageStream());
        searchOperations.uploadSearch(uploadSearchParams);
        Multimap<String, String> params = HashMultimap.create();
        verify(mockClient).postImage(eq("/uploadsearch"), eq(params), Matchers.<byte[]>any(), eq("image-stream"));
    }

    @Test
    public void testUploadSearchParamsBox() {
        String response = "{\"status\":\"OK\",\"method\":\"upload\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        when(mockClient.postImage(anyString(), Matchers.<Multimap<String, String>>any(), Matchers.<byte[]>any(), anyString())).thenReturn(response);
        SearchOperations searchOperations = new SearchOperationsImpl(mockClient, objectMapper);
        URL resourceUrl = getClass().getResource("/1600x1600.jpeg");
        File imageFile = new File(resourceUrl.getFile());
        UploadSearchParams uploadSearchParams = new UploadSearchParams(imageFile);
        Box box = new Box(400, 400, 800, 800);
        uploadSearchParams.setBox(box);
        searchOperations.uploadSearch(uploadSearchParams);
        Multimap<String, String> params = HashMultimap.create();
        params.put("box", "128,128,256,256");
        verify(mockClient).postImage(eq("/uploadsearch"), eq(params), Matchers.<byte[]>any(), eq(imageFile.getName()));
    }

    @Test
    public void testResizeParamQuality() throws Exception {
        ResizeSettings resizeSettings = new ResizeSettings(100, 100, -1);
        assertEquals(0, resizeSettings.getQuality());
        ResizeSettings resizeSettings1 = new ResizeSettings(100, 100, 101);
        assertEquals(100, resizeSettings1.getQuality());
    }
}
