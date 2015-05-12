package com.visenze.visearch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.visenze.visearch.internal.DataOperations;
import com.visenze.visearch.internal.SearchOperations;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ViSearchTest {

    private ViSearch visearch;
    private DataOperations dataOperations;
    private SearchOperations searchOperations;

    @Before
    public void setup() throws Exception {
        dataOperations = mock(DataOperations.class);
        searchOperations = mock(SearchOperations.class);
        visearch = new ViSearch(dataOperations, searchOperations);
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
    public void testInsertStatus1() throws Exception {
        visearch.insertStatus("transId", 1, 10);
        verify(dataOperations).insertStatus("transId", 1, 10);
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
    public void testUploadSearch1() throws Exception {
        UploadSearchParams uploadSearchParams = new UploadSearchParams(new File("/tmp/test_image.jpg"));
        ResizeSettings resizeSettings = new ResizeSettings(500, 500, 80);
        visearch.uploadSearch(uploadSearchParams, resizeSettings);
        verify(searchOperations).uploadSearch(uploadSearchParams, resizeSettings);
    }
}