package com.visenze.visearch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hung on 18/2/20.
 */
public class DiversityQueryTest {
    @Test
    public void toParamValue() throws Exception {
        DiversityQuery testField = new DiversityQuery();
        testField.setField("store_id");
        testField.setValue("1234");
        testField.setRatio(44);

        assertEquals("store_id:1234:44", testField.toParamValue());
    }

}