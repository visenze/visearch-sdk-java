package com.visenze.visearch;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.TrackOperationsImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ViSearchTrackOperationsTest {

    private ViSearchHttpClient mockClient;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void beforeTest() {
        mockClient = mock(ViSearchHttpClient.class);
    }

    @Test
    public void testSearchParamsBasic() throws InterruptedException {
        String responseBody = "{\"status\":\"OK\",\"method\":\"search\",\"error\":[],\"page\":1,\"limit\":10,\"total\":20,\"result\":[{\"im_name\":\"test_im_1\"}]}";
        ViSearchHttpResponse response = mock(ViSearchHttpResponse.class);
        when(response.getBody()).thenReturn(responseBody);
        Map<String, String> responseHeaders = Maps.newHashMap();
        responseHeaders.put("test-param", "123");
        when(response.getHeaders()).thenReturn(responseHeaders);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(response);
        TrackOperationsImpl trackOperations = new TrackOperationsImpl(mockClient);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "click");
        params.put("reqid", "1111");
        params.put("im_name", "imName1111");
        trackOperations.sendEvent(params);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("action", "click");
        expectedParams.put("reqid", "1111");
        expectedParams.put("im_name", "imName1111");
        expectedParams.put("cid", "");
        Thread.sleep(200);
        verify(mockClient).get("/__aq.gif", expectedParams);
    }

}
