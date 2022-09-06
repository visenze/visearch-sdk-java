package com.visenze.visearch;

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
        String fakeUserContext = "{\"session\":{\"sid\": \"Hi6zB1wmFtoe8wGNyknD4oNVW6uRf2LYP-MYDLpbIi0wJSpc\",\"events\": [{\"action\": \"result_load\",\"pid\": \"2\",\"ts\": 1632473768000},{\"action\": \"add_to_cart\",\"pid\": \"2\",\"ts\": 1632473760000},{\"action\": \"transaction\",\"pid\": \"2\",\"ts\": 1632473768000}]}}";
        RecommendSearchParams recommendSearchParams = new RecommendSearchParams("im_name");
        recommendSearchParams.setAlgorithm("STL");
        recommendSearchParams.setAltLimit(10);
        recommendSearchParams.setDedupBy("parent_id");
        recommendSearchParams.setStrategyId("test_strategy");
        recommendSearchParams.setUserContext(fakeUserContext);
        recommendSearchParams.setSetLimit(20);
        recommendSearchParams.setUseSetBasedCtl(false);
        Multimap<String, String> params = recommendSearchParams.toMap();
        assertArrayEquals(new String[]{"im_name"}, params.get("im_name").toArray(new String[0]));
        assertArrayEquals(new String[]{"STL"}, params.get("algorithm").toArray(new String[0]));
        assertArrayEquals(new String[]{"10"}, params.get("alt_limit").toArray(new String[0]));
        assertArrayEquals(new String[]{"parent_id"}, params.get("dedup_by").toArray(new String[0]));
        assertArrayEquals(new String[]{"test_strategy"}, params.get("strategy_id").toArray(new String[0]));
        assertArrayEquals(new String[]{fakeUserContext}, params.get("user_context").toArray(new String[0]));

        assertArrayEquals(new String[]{"20"}, params.get("set_limit").toArray(new String[0]));
        assertArrayEquals(new String[]{"false"}, params.get("use_set_based_ctl").toArray(new String[0]));

    }

    @Test
    public void testToMapByDefaultValue() {
        RecommendSearchParams recommendSearchParams = new RecommendSearchParams("im_name");
        Multimap<String, String> params = recommendSearchParams.toMap();
        assertArrayEquals(new String[]{"im_name"}, params.get("im_name").toArray(new String[0]));
        assertArrayEquals(new String[]{}, params.get("algorithm").toArray(new String[0]));
        assertArrayEquals(new String[]{}, params.get("alt_limit").toArray(new String[0]));
        assertArrayEquals(new String[]{}, params.get("dedup_by").toArray(new String[0]));
        assertArrayEquals(new String[]{}, params.get("strategy_id").toArray(new String[0]));
        assertArrayEquals(new String[]{}, params.get("user_context").toArray(new String[0]));
    }
}
