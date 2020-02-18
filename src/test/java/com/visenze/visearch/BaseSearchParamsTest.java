package com.visenze.visearch;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hung on 18/2/20.
 */
public class BaseSearchParamsTest {
    @Test
    public void toMap() throws Exception {
        BaseSearchParams params = new BaseSearchParams();

        String field = "store_id" ;

        DiversityQuery testField1 = new DiversityQuery();
        testField1.setField(field);
        testField1.setValue("123");
        testField1.setRatio(4);

        DiversityQuery testField2 = new DiversityQuery();
        testField2.setField(field);
        testField2.setValue("124");
        testField2.setRatio(5);


        params.setDiversityQueries(Lists.newArrayList(testField1, testField2));

        Multimap<String, String> map = params.toMap();
        String[] values = map.get("diversity").toArray(new String[0]);;

        assertEquals("store_id:123:4", values[0]);
        assertEquals("store_id:124:5", values[1]);


    }

}