package com.visenze.productsearch.param;

import com.google.common.collect.Multimap;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

public class BaseSearchParamTest extends TestCase {

    @Test
    public void testToMap() {

        BaseSearchParam params = new BaseSearchParam();

        // touch list
        List<String> attrs = new ArrayList<String>();
        attrs.add("attr 1");
        attrs.add("attr 2");
        params.setAttrsToGet(attrs);

        // touch bool
        params.setDebug(Boolean.TRUE);

        // touch int
        params.setFacetsLimit(5);

        // touch string
        params.setVaSdk("Some_SDK");

        // touch map
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("key_1","value_1");
        filters.put("key_2","value_2");
        filters.put("key_3","value_3");
        params.setFilters(filters);

        // touch float
        params.setScoreMin(0.4f);

        Multimap<String, String> map = params.toMultimap();

        map.get("filters");
        map.get("debug");
        map.get("attrs_to_get");
        map.get("facets_limit");
        map.get("va_sdk");
        map.get("score_min");

        assertEquals("pid", map.get("placement_id").toArray()[0]);
    }

}