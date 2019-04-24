package com.visenze.visearch;

import com.google.common.collect.Multimap;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by zhumingyuan on 2019-04-24
 */
public class MatchSearchParamsTest {

    @Test
    public void testToMap() {
        MatchSearchParams matchSearchParams = new MatchSearchParams("im_name");
        matchSearchParams.setObjectLimit(10);
        Multimap<String, String> params = matchSearchParams.toMap();
        assertArrayEquals(new String[]{"im_name"}, params.get("im_name").toArray(new String[0]));
        assertArrayEquals(new String[]{"10"}, params.get("object_limit").toArray(new String[0]));
    }
}
