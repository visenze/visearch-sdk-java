package com.visenze.visearch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.visenze.visearch.internal.DataOperations;
import com.visenze.visearch.internal.SearchOperations;
import com.visenze.visearch.internal.TrackOperations;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ViSearch.class, ViSearchHttpClientImpl.class})
@PowerMockIgnore("javax.net.ssl.*")
public class ViSearchTest {

    private ViSearch visearch;
    private DataOperations dataOperations;
    private SearchOperations searchOperations;
    private TrackOperations trackOperations;

    @Before
    public void setup() throws Exception {
        dataOperations = mock(DataOperations.class);
        searchOperations = mock(SearchOperations.class);
        trackOperations = mock(TrackOperations.class);
        visearch = new ViSearch(dataOperations, searchOperations, trackOperations);
    }

    @Test
    public void testVersion() {
        assertNotEquals(ViSearch.VISEACH_JAVA_SDK_VERSION, "1.3.0");
    }


    @Test
    public void testInitWithEndpoint() throws Exception {
        PowerMockito.mock(ViSearchHttpClientImpl.class);
        PowerMockito.whenNew(ViSearchHttpClientImpl.class).withAnyArguments().thenReturn(Mockito.mock(ViSearchHttpClientImpl.class));
        ViSearch viSearch = new ViSearch("localhost", "accessKey", "secretKey");
        verifyNew(ViSearchHttpClientImpl.class).withArguments(eq("localhost"), eq("accessKey"), eq("secretKey"));
    }

    @Test
    public void testInitWithClientConfig() throws Exception {
        PowerMockito.mock(ViSearchHttpClientImpl.class);
        PowerMockito.whenNew(ViSearchHttpClientImpl.class).withAnyArguments().thenReturn(Mockito.mock(ViSearchHttpClientImpl.class));
        ViSearch viSearch = new ViSearch("localhost", "accessKey", "secretKey", new ClientConfig());
        verifyNew(ViSearchHttpClientImpl.class).withArguments(eq("localhost"), eq("accessKey"), eq("secretKey"), Matchers.any(ClientConfig.class));
    }

    @Test
    public void testInsert() throws Exception {
        List<Image> imageList = Lists.newArrayList();
        visearch.insert(imageList);
        verify(dataOperations).insert(imageList);
    }

    @Test
    public void testInsert1() throws Exception {
        List<Image> imageList = Lists.newArrayList();
        Map<String, String> customParams = Maps.newHashMap();
        visearch.insert(imageList, customParams);
        verify(dataOperations).insert(imageList, customParams);
    }

    @Test
    public void testInsertStatus() throws Exception {
        visearch.insertStatus("transId");
        verify(dataOperations).insertStatus("transId");
    }

    @Test
    public void testInsertStatusWithParams() {
        Map<String, String> customParams = Maps.newHashMap();
        visearch.insertStatus("transId", customParams);
        verify(dataOperations).insertStatus("transId", customParams);
    }

    @Test
    public void testInsertStatus1() throws Exception {
        visearch.insertStatus("transId", 1, 10);
        verify(dataOperations).insertStatus("transId", 1, 10);
    }

    @Test
    public void testInsertStatusGetErrorPageWithParams() {
        Map<String, String> customParams = Maps.newHashMap();
        visearch.insertStatus("transId", 1, 10, customParams);
        verify(dataOperations).insertStatus("transId", 1, 10, customParams);
    }

    @Test
    public void testRemove() throws Exception {
        List<String> removeList = Lists.newArrayList();
        visearch.remove(removeList);
        verify(dataOperations).remove(removeList);
    }

    @Test
    public void testSearch() throws Exception {
        SearchParams searchParams = new SearchParams("test_name");
        visearch.search(searchParams);
        verify(searchOperations).search(searchParams);
    }

    @Test
    public void testColorSearch() throws Exception {
        ColorSearchParams colorSearchParams = new ColorSearchParams("123ABC");
        visearch.colorSearch(colorSearchParams);
        verify(searchOperations).colorSearch(colorSearchParams);
    }

    @Test
    public void testUploadSearch() throws Exception {
        UploadSearchParams uploadSearchParams = new UploadSearchParams(new File("/tmp/test_image.jpg"));
        visearch.uploadSearch(uploadSearchParams);
        verify(searchOperations).uploadSearch(uploadSearchParams);
    }

    @Test
    public void testUploadSearchWithDedup() throws Exception {
        UploadSearchParams uploadSearchParams = new UploadSearchParams(new File("/tmp/test_image.jpg"));
        uploadSearchParams.setDedup(true);
        uploadSearchParams.setDedupThreshold(0.0001F);
        visearch.uploadSearch(uploadSearchParams);
        verify(searchOperations).uploadSearch(uploadSearchParams);
    }

    @Test
    public void testDiscoverSearch() throws Exception {
        UploadSearchParams similarProductsSearchParams = new UploadSearchParams(new File("/tmp/test_image.jpg"));
        visearch.discoverSearch(similarProductsSearchParams);
        verify(searchOperations).discoverSearch(similarProductsSearchParams);
    }

    @Test
    public void testSendEvent() {
        Map<String,String> params = new HashMap<String,String>();
        params.put("action","click");
        params.put("reqid","543577997719293952");
        params.put("im_name","xyool-12-9");
        visearch.sendEvent(params);
        verify(trackOperations).sendEvent(params);
    }

    @Test
    public void testWithoutAutoSolutionAction() throws InterruptedException {
        UploadSearchParams uploadSearchParams = new UploadSearchParams(new File("/tmp/test_image.jpg"));
        uploadSearchParams.setDedup(true);
        uploadSearchParams.setDedupThreshold(0.0001F);
        when(searchOperations.uploadSearch(any(UploadSearchParams.class))).then(new Answer<PagedSearchResult>() {
            @Override
            public PagedSearchResult answer(InvocationOnMock invocationOnMock) throws Throwable {
                PagedSearchResult result = new PagedSearchResult(null);
                result.setHeaders(ImmutableMap.of("X-Log-ID","11111"));
                return result;
            }
        });
        // with auto solution action
        visearch.uploadSearch(uploadSearchParams);
        verify(trackOperations, new Times(1)).sendEvent(any(Map.class));
        visearch.setEnableAutoSolutionActionTrack(false);

        // without auto solution action
        reset(trackOperations);
        visearch.uploadSearch(uploadSearchParams);
        verify(trackOperations, new Times(0)).sendEvent(any(Map.class));
    }

    @Test
    public void testDiscoverSearchWithoutAutoSolutionAction() throws InterruptedException {
        UploadSearchParams uploadSearchParams = new UploadSearchParams(new File("/tmp/test_image.jpg"));
        when(searchOperations.discoverSearch(any(UploadSearchParams.class))).then(new Answer<PagedSearchResult>() {
            @Override
            public PagedSearchResult answer(InvocationOnMock invocationOnMock) throws Throwable {
                PagedSearchResult result = new PagedSearchResult(null);
                result.setHeaders(ImmutableMap.of("X-Log-ID","11111"));
                return result;
            }
        });
        // with auto solution action
        visearch.discoverSearch(uploadSearchParams);
        verify(trackOperations, new Times(1)).sendEvent(any(Map.class));
    }

    @Test
    public void testSimilarSearchWithoutAutoSolutionAction() throws InterruptedException {
        UploadSearchParams uploadSearchParams = new UploadSearchParams(new File("/tmp/test_image.jpg"));
        when(searchOperations.similarProductsSearch(any(UploadSearchParams.class))).then(new Answer<PagedSearchResult>() {
            @Override
            public PagedSearchResult answer(InvocationOnMock invocationOnMock) throws Throwable {
                PagedSearchResult result = new PagedSearchResult(null);
                result.setHeaders(ImmutableMap.of("X-Log-ID","11111"));
                return result;
            }
        });
        // with auto solution action
        visearch.similarProductsSearch(uploadSearchParams);
        verify(searchOperations, new Times(1)).similarProductsSearch(any(UploadSearchParams.class));
    }
}
