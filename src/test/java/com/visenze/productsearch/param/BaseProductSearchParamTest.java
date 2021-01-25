package com.visenze.productsearch.param;

import com.visenze.visearch.internal.http.ViSearchHttpClientImpl;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import static org.junit.Assert.*;

/**
 * <h1> BaseProductSearchParam Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 21 Jan 2021
 */
public class BaseProductSearchParamTest {
    final String END_POINT = "https://search-dev.visenze.com/v1/similar-products?";
    final String PARAM_DESIRED_SIMPLE = "app_key=APP_KEY&placement_id=1";
    final String PARAM_DESIRED_COMPLEX = "app_key=APP_KEY&placement_id=1&return_fields_mapping=true&score=true&text_filters=filter_field_2:filter_field_2_value&text_filters=filter_field_1:filter_field_1_value";

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

    @Test
    public void simpleParameters() {
        BaseProductSearchParam param = new BaseProductSearchParam();
        List<NameValuePair> paramUrl = sort(ViSearchHttpClientImpl.mapToNameValuePair(param.toMultimap()));
        try{
            URI uri = new URIBuilder(END_POINT).addParameters(paramUrl).build();
            assertEquals(PARAM_DESIRED_SIMPLE, uri.getQuery());
        } catch (URISyntaxException e) {
            fail("Failed to create proper query");
        }
    }

    @Test
    public void complexParameters() {
        BaseProductSearchParam param = new BaseProductSearchParam();
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        Map<String, String> filters = new HashMap<String, String>();
        filters.put("filter_field_1", "filter_field_1_value");
        filters.put("filter_field_2", "filter_field_2_value");
        param.setTextFilters(filters);

        List<NameValuePair> paramUrl = sort(ViSearchHttpClientImpl.mapToNameValuePair(param.toMultimap()));
        try{
            URI uri = new URIBuilder(END_POINT).addParameters(paramUrl).build();
            assertEquals(PARAM_DESIRED_COMPLEX, uri.getQuery());
        } catch (URISyntaxException e) {
            fail("Failed to create proper query");
        }
    }
}