package com.visenze.productsearch;

import com.google.common.collect.Multimap;
import com.visenze.productsearch.http.ProductSearchHttpClientImpl;
import com.visenze.productsearch.param.SearchByIdParam;
import com.visenze.productsearch.param.SearchByImageParam;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductSearchPathRoutingTest {

    private static final String DUMMY_RESPONSE = "{\"status\":\"OK\",\"im_id\":\"test.jpg\",\"reqid\":\"r1\"}";
    private static final String DUMMY_KEY = "test_key";
    private static final int DUMMY_PLACEMENT = 1;

    private ProductSearchHttpClientImpl mockClient;
    private ViSearchHttpResponse mockResponse;

    @Before
    public void setUp() {
        mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(DUMMY_RESPONSE);
        mockClient = mock(ProductSearchHttpClientImpl.class);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
    }

    private ProductSearch buildSdk(String endpoint) {
        ProductSearch sdk = new ProductSearch.Builder(DUMMY_KEY, DUMMY_PLACEMENT)
                .setApiEndPoint(endpoint)
                .build();
        sdk.setHttpClient(mockClient);
        return sdk;
    }

    // --- Legacy endpoint path tests ---

    @Test
    public void legacyEndpoint_imageSearch_usesLegacyPath() {
        ProductSearch sdk = buildSdk("https://search.visenze.com");
        sdk.imageSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/product/search_by_image"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void legacyEndpoint_multiSearch_usesLegacyPath() {
        ProductSearch sdk = buildSdk("https://search.visenze.com");
        sdk.multiSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/product/multisearch"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void legacyEndpoint_multiSearchAutocomplete_usesLegacyPath() {
        ProductSearch sdk = buildSdk("https://search.visenze.com");
        sdk.multiSearchAutocomplete(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/product/multisearch/autocomplete"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void legacyEndpoint_visualSimilarSearch_usesLegacyPath() {
        ProductSearch sdk = buildSdk("https://search.visenze.com");
        sdk.visualSimilarSearch(new SearchByIdParam("pid123"));
        verify(mockClient).get(eq("/v1/product/search_by_id/pid123"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void legacyEndpoint_recommendations_usesLegacyPath() {
        ProductSearch sdk = buildSdk("https://search.visenze.com");
        sdk.recommendations(new SearchByIdParam("pid123"));
        verify(mockClient).get(eq("/v1/product/recommendations/pid123"), Matchers.<Multimap<String, String>>any());
    }

    // --- AWS new endpoint path tests ---

    @Test
    public void awsEndpoint_imageSearch_usesNewPath() {
        ProductSearch sdk = buildSdk(ProductSearch.ENDPOINT_AWS);
        sdk.imageSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/visearch/search_by_image"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void awsEndpoint_multiSearch_usesNewPath() {
        ProductSearch sdk = buildSdk(ProductSearch.ENDPOINT_AWS);
        sdk.multiSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/search"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void awsEndpoint_multiSearchAutocomplete_usesNewPath() {
        ProductSearch sdk = buildSdk(ProductSearch.ENDPOINT_AWS);
        sdk.multiSearchAutocomplete(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/autocomplete"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void awsEndpoint_visualSimilarSearch_usesNewPath() {
        ProductSearch sdk = buildSdk(ProductSearch.ENDPOINT_AWS);
        sdk.visualSimilarSearch(new SearchByIdParam("pid123"));
        verify(mockClient).get(eq("/v1/visearch/search_by_id/pid123"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void awsEndpoint_recommendations_usesNewPath() {
        ProductSearch sdk = buildSdk(ProductSearch.ENDPOINT_AWS);
        sdk.recommendations(new SearchByIdParam("pid123"));
        verify(mockClient).get(eq("/v1/visearch/recommendations/pid123"), Matchers.<Multimap<String, String>>any());
    }

    // --- Azure new endpoint path tests ---

    @Test
    public void azureEndpoint_imageSearch_usesNewPath() {
        ProductSearch sdk = buildSdk(ProductSearch.ENDPOINT_AZURE);
        sdk.imageSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/visearch/search_by_image"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void azureEndpoint_multiSearch_usesNewPath() {
        ProductSearch sdk = buildSdk(ProductSearch.ENDPOINT_AZURE);
        sdk.multiSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/search"), Matchers.<Multimap<String, String>>any());
    }

    // --- Builder convenience methods ---

    @Test
    public void builderUseAws_setsAwsEndpoint() {
        ProductSearch sdk = new ProductSearch.Builder(DUMMY_KEY, DUMMY_PLACEMENT)
                .useAws()
                .build();
        sdk.setHttpClient(mockClient);
        sdk.imageSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/visearch/search_by_image"), Matchers.<Multimap<String, String>>any());
    }

    @Test
    public void builderUseAzure_setsAzureEndpoint() {
        ProductSearch sdk = new ProductSearch.Builder(DUMMY_KEY, DUMMY_PLACEMENT)
                .useAzure()
                .build();
        sdk.setHttpClient(mockClient);
        sdk.imageSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/visearch/search_by_image"), Matchers.<Multimap<String, String>>any());
    }

    // --- Trailing slash tolerance ---

    @Test
    public void awsEndpointWithTrailingSlash_stillUsesNewPaths() {
        ProductSearch sdk = buildSdk("https://multisearch-aw.rezolve.com/");
        sdk.imageSearch(SearchByImageParam.newFromImageUrl("http://example.com/img.jpg"));
        verify(mockClient).post(eq("/v1/visearch/search_by_image"), Matchers.<Multimap<String, String>>any());
    }
}
