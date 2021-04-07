package com.visenze.productsearch.param;

import com.visenze.visearch.Box;
import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.*;

/**
 * <h1> ImageSearchParam Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 21 Jan 2021
 */
public class SearchByImageParamTest {
    final String PARAM_URL = "box=1,1,5,5&im_url=SOME_URL";
    final String PARAM_ID = "box=2,3,4,5&im_id=SOME_ID";

    @Test
    public void setterGetter() {
        SearchByImageParam param = SearchByImageParam.newFromImageId("IMAGE_ID");
        assertEquals("IMAGE_ID", param.getImageId());

        param.setImageId("IMAGE_ID_2");
        assertEquals("IMAGE_ID_2", param.getImageId());

        param.setImageUrl("IMAGE_URL");
        assertEquals("IMAGE_URL", param.getImageUrl());

        param.setDetection("all");
        assertEquals("all", param.getDetection());

        Integer limit = 15;
        param.setDetectionLimit(limit);
        assertEquals(limit, param.getDetectionLimit());

        param.setDetectionSensitivity("high");
        assertEquals("high", param.getDetectionSensitivity());

        param.setSearchAllObjects(true);
        assertEquals(true, param.getSearchAllObjects());
    }

    @Test
    public void toMultimap() {
        SearchByImageParam param = SearchByImageParam.newFromImageUrl("SOME_URL");
        param.setBox(new Box(1,1,5,5));

        List<NameValuePair> paramUrl = sort(ViSearchHttpClientImpl.mapToNameValuePair(param.toMultimap()));
        try{
            URI uri = new URIBuilder("www.example.com").addParameters(paramUrl).build();
            assertEquals(PARAM_URL, uri.getQuery());
        } catch (URISyntaxException e) {
            fail("Failed to create proper query");
        }

        param = SearchByImageParam.newFromImageId("SOME_ID");
        param.setBox(new Box(2,3,4,5));
        paramUrl = sort(ViSearchHttpClientImpl.mapToNameValuePair(param.toMultimap()));
        try{
            URI uri = new URIBuilder("www.example.com").addParameters(paramUrl).build();
            assertEquals(PARAM_ID, uri.getQuery());
        } catch (URISyntaxException e) {
            fail("Failed to create proper query");
        }
    }

    /**
     * Sort List<NameValuePair> because multimap has no definite order to test
     * with 1-to-1 match against the desired parameter queries.
     */
    private List<NameValuePair> sort(List<NameValuePair> unsorted) {
        Comparator<NameValuePair> comp = new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair p1, NameValuePair p2) {
                return p1.getName().compareTo(p2.getName());
            }
        };
        Collections.sort(unsorted, comp);
        return unsorted;
    }
}