package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.DataOperations;
import com.visenze.visearch.internal.DataOperationsImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.json.ViSearchModule;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ViSearchInsertTest {

    private ViSearchHttpClient mockClient;
    private ObjectMapper objectMapper;

    @Before
    public void beforeTest() {
        mockClient = mock(ViSearchHttpClient.class);
        objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
    }

    @Test
    public void testInsertNoImage() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":0,\"error\":[{\"error_code\":104,\"error_message\":\"No image inserted.\"}]}";
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(0), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNotNull(insertTrans.getErrorList());
        List<InsertError> errorList = insertTrans.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals(new Integer(104), error.getErrorCode());
        assertEquals("No image inserted.", error.getErrorMessage());
    }

    @Test
    public void testInsertOneImageSuccess() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"trans_id\":317503499455827968,\"method\":\"insert\",\"total\":1}";
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(1), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNull(insertTrans.getErrorList());
    }

    @Test
    public void testInsertOneImageFail() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":0,\"error\":[{\"index\":0,\"im_name\":\"test im\",\"error_code\":107,\"error_message\":\"Invalid im_name.\"}]}";
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(0), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNotNull(insertTrans.getErrorList());
        List<InsertError> errorList = insertTrans.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals(new Integer(0), error.getIndex());
        assertEquals(new Integer(107), error.getErrorCode());
        assertEquals("Invalid im_name.", error.getErrorMessage());
    }

    @Test
    public void testInsertMultipleImagesMix() throws Exception {
        String insertResponse = "{\"status\":\"warning\",\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":1,\"error\":[{\"index\":1,\"im_name\":\"test im\",\"error_code\":107,\"error_message\":\"Invalid im_name.\"}]}";
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(insertResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(1), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNotNull(insertTrans.getErrorList());
        List<InsertError> errorList = insertTrans.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals(new Integer(107), error.getErrorCode());
        assertEquals("Invalid im_name.", error.getErrorMessage());
    }

    @Test
    public void testInsertStatusFail() throws Exception {
        String insertStatusResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":0,\"fail_count\":2,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im_0\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"},{\"im_name\":\"test_im_1\",\"error_code\":202,\"error_message\":\"Unsupported image format.\"}],\"error_page\":1,\"error_limit\":10}]}";
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(insertStatusResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals("317503499455827968", insertStatus.getTransId());
        assertEquals(new Integer(100), insertStatus.getProcessedPercent());
        assertEquals(new Integer(2), insertStatus.getTotal());
        assertEquals(new Integer(0), insertStatus.getSuccessCount());
        assertEquals(new Integer(2), insertStatus.getFailCount());
        assertEquals(new Integer(1), insertStatus.getErrorPage());
        assertEquals(new Integer(10), insertStatus.getErrorLimit());
        assertTrue(insertStatus.getStartTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertTrue(insertStatus.getUpdateTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertNotNull(insertStatus.getErrorList());
        List<InsertError> errorList = insertStatus.getErrorList();
        assertEquals(2, errorList.size());
        InsertError error0 = errorList.get(0);
        assertEquals("test_im_0", error0.getImName());
        assertEquals(new Integer(201), error0.getErrorCode());
        assertEquals("Could not download the image from im_url.", error0.getErrorMessage());
        InsertError error1 = errorList.get(1);
        assertEquals("test_im_1", error1.getImName());
        assertEquals(new Integer(202), error1.getErrorCode());
        assertEquals("Unsupported image format.", error1.getErrorMessage());
    }

    @Test
    public void testInsertStatusSuccess() throws Exception {
        String insertStatusResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":2,\"fail_count\":0,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\"}]}";
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(insertStatusResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals("317503499455827968", insertStatus.getTransId());
        assertEquals(new Integer(100), insertStatus.getProcessedPercent());
        assertEquals(new Integer(2), insertStatus.getTotal());
        assertEquals(new Integer(2), insertStatus.getSuccessCount());
        assertEquals(new Integer(0), insertStatus.getFailCount());
        assertTrue(insertStatus.getStartTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertTrue(insertStatus.getUpdateTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertNull(insertStatus.getErrorPage());
        assertNull(insertStatus.getErrorLimit());
        assertNull(insertStatus.getErrorList());
    }

    @Test
    public void testInsertStatusMixed() throws Exception {
        String insertStatusResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":1,\"fail_count\":1,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"}],\"error_page\":1,\"error_limit\":10}]}";
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(insertStatusResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals("317503499455827968", insertStatus.getTransId());
        assertEquals(new Integer(100), insertStatus.getProcessedPercent());
        assertEquals(new Integer(2), insertStatus.getTotal());
        assertEquals(new Integer(1), insertStatus.getSuccessCount());
        assertEquals(new Integer(1), insertStatus.getFailCount());
        assertTrue(insertStatus.getStartTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertTrue(insertStatus.getUpdateTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertEquals(new Integer(1), insertStatus.getErrorPage());
        assertEquals(new Integer(10), insertStatus.getErrorLimit());
        assertNotNull(insertStatus.getErrorList());
        List<InsertError> errorList = insertStatus.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals("test_im", error.getImName());
        assertEquals(new Integer(201), error.getErrorCode());
        assertEquals("Could not download the image from im_url.", error.getErrorMessage());
    }
}