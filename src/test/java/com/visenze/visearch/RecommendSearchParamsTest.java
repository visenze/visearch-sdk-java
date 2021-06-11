package com.visenze.visearch;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by david.huang on 2021-06-04
 */
public class RecommendSearchParamsTest {

    @Test
    public void testToMap() {
        RecommendSearchParams recommendSearchParams = new RecommendSearchParams("im_name");
        recommendSearchParams.setRecommendationAlgorithm("STL");
        recommendSearchParams.setAltLimit(10);
        recommendSearchParams.setDedupBy(Lists.newArrayList("parent_id"));
        Multimap<String, String> params = recommendSearchParams.toMap();
        assertArrayEquals(new String[]{"im_name"}, params.get("im_name").toArray(new String[0]));
        assertArrayEquals(new String[]{"STL"}, params.get("recommendation_algorithm").toArray(new String[0]));
        assertArrayEquals(new String[]{"10"}, params.get("alt_limit").toArray(new String[0]));
        assertArrayEquals(new String[]{"parent_id"}, params.get("dedup_by").toArray(new String[0]));
    }

    @Test
    public void testToMapByDefaultValue() {
        RecommendSearchParams recommendSearchParams = new RecommendSearchParams("im_name");
        Multimap<String, String> params = recommendSearchParams.toMap();
        assertArrayEquals(new String[]{"im_name"}, params.get("im_name").toArray(new String[0]));
        assertArrayEquals(new String[]{}, params.get("recommendation_algorithm").toArray(new String[0]));
        assertArrayEquals(new String[]{"5"}, params.get("alt_limit").toArray(new String[0]));
        assertArrayEquals(new String[]{}, params.get("dedup_by").toArray(new String[0]));
    }
}
