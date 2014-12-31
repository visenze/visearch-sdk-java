package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.DataOperations;
import com.visenze.visearch.internal.DataOperationsImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.json.ViSearchModule;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class ViSearchTest {

    @Test
    public void testInsertEmpty() throws Exception {
        ViSearchHttpClient mockClient = mock(ViSearchHttpClient.class);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        String insertResponse = "{\"status\":\"fail\",\"trans_id\":317503499455827968,\"method\":\"insert\",\"total\":0,\"error\":[{\"error_code\":104,\"error_message\":\"No image inserted.\"}]}";
        when(mockClient.post(
                anyString(),
                Matchers.<Multimap<String, String>>any())
        )
        .thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper, "http://visearch.visenze.com");
        List<Image> imageList = new ArrayList<Image>();
        InsertTransaction insertTransaction = dataOperations.insert(imageList);
        assertEquals(new Integer(0), insertTransaction.getTotal());
        assertEquals("317503499455827968", insertTransaction.getTransactionId());
        assertNull(insertTransaction.getSuccessCount());
        assertNull(insertTransaction.getFailedCount());
        assertNull(insertTransaction.getStartTime());
        assertNull(insertTransaction.getUpdateTime());
        assertNotNull(insertTransaction.getError());
    }

    @Test
    public void testInsertImage() throws Exception {
        ViSearchHttpClient mockClient = mock(ViSearchHttpClient.class);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        String insertResponse = "{\"status\":\"OK\",\"trans_id\":317517564605501440,\"method\":\"insert\",\"total\":1}";
        when(mockClient.post(
                        anyString(),
                        Matchers.<Multimap<String, String>>any())
        )
                .thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper, "http://visearch.visenze.com");
        List<Image> imageList = new ArrayList<Image>();
        imageList.add(new Image("test_imname", "http://visenze.com/test_im_url.jpg", new HashMap<String, String>()));
        InsertTransaction insertTransaction = dataOperations.insert(imageList);
        assertEquals(new Integer(1), insertTransaction.getTotal());
        assertEquals("317517564605501440", insertTransaction.getTransactionId());
        assertNull(insertTransaction.getSuccessCount());
        assertNull(insertTransaction.getFailedCount());
        assertNull(insertTransaction.getStartTime());
        assertNull(insertTransaction.getUpdateTime());
        assertNull(insertTransaction.getError());
    }

    @Test
    public void testInsertStatusFailedImage() throws Exception {
        ViSearchHttpClient mockClient = mock(ViSearchHttpClient.class);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317506403860353024,\"total\":2,\"success_count\":0,\"fail_count\":2,\"start_time\":\"2014-12-31T10:35:31.183+0000\",\"update_time\":\"2014-12-31T10:35:31.183+0000\",\"error_list\":[{\"im_name\":\"test_imname1\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"},{\"im_name\":\"test_imname\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"}],\"error_page\":1,\"error_limit\":2,\"error_total\":2}]}";
        when(mockClient.get(
                        anyString(),
                        Matchers.<Multimap<String, String>>any())
        )
                .thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper, "http://visearch.visenze.com");
        InsertTransaction insertTransaction = dataOperations.getStatus("317506403860353024");
        assertEquals(new Integer(0), insertTransaction.getSuccessCount());
        assertEquals(new Integer(2), insertTransaction.getFailedCount());
        assertEquals(new Integer(2), insertTransaction.getTotal());
        assertEquals("317506403860353024", insertTransaction.getTransactionId());
        assertNotNull(insertTransaction.getStartTime());
        assertNotNull(insertTransaction.getUpdateTime());
        assertNotNull(insertTransaction.getError());
    }

    @Test
    public void testInsertStatusSuccessImage() throws Exception {
        ViSearchHttpClient mockClient = mock(ViSearchHttpClient.class);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317071016692158464,\"total\":2,\"success_count\":2,\"fail_count\":0,\"start_time\":\"2014-12-30T05:45:26.807+0000\",\"update_time\":\"2014-12-30T05:45:32.986+0000\"}]}";
        when(mockClient.get(
                        anyString(),
                        Matchers.<Multimap<String, String>>any())
        )
                .thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper, "http://visearch.visenze.com");
        InsertTransaction insertTransaction = dataOperations.getStatus("317071016692158464");
        assertEquals(new Integer(2), insertTransaction.getSuccessCount());
        assertEquals(new Integer(0), insertTransaction.getFailedCount());
        assertEquals(new Integer(2), insertTransaction.getTotal());
        assertEquals("317071016692158464", insertTransaction.getTransactionId());
        assertNotNull(insertTransaction.getStartTime());
        assertNotNull(insertTransaction.getUpdateTime());
        assertNotNull(insertTransaction.getError());
    }

}