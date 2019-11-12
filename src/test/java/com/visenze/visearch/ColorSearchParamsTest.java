package com.visenze.visearch;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhumingyuan on 11/10/19
 */
public class ColorSearchParamsTest {

    @Test
    public void testColorSearchParamsGetColor() {
        ColorSearchParams params = new ColorSearchParams("123123");
        assertEquals("123123", params.getColor());
    }

    @Test
    public void testColorSearchParamsGetColors() {
        ColorSearchParams.ColorAndWeight colorAndWeight1 = new ColorSearchParams.ColorAndWeight("000000", 10);
        ColorSearchParams.ColorAndWeight colorAndWeight2 = new ColorSearchParams.ColorAndWeight("ffffff", 20);
        ColorSearchParams params =
            new ColorSearchParams(Lists.newArrayList(colorAndWeight1, colorAndWeight2));
        assertEquals(Lists.newArrayList(colorAndWeight1, colorAndWeight2), params.getColorAndWeights());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testColorSearchParamsInvalidColor() {
        ColorSearchParams colorSearchParams = new ColorSearchParams("gac");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testColorSearchParamsInvalidColors() {
        List<ColorSearchParams.ColorAndWeight> colors = Lists.newArrayList();
        ColorSearchParams colorSearchParams = new ColorSearchParams(colors);
    }

    @Test
    public void testColorAndWeightsGetParams() {
        ColorSearchParams.ColorAndWeight colorAndWeight = new ColorSearchParams.ColorAndWeight("123123");
        assertEquals("123123", colorAndWeight.getColor());
        assertEquals(0, colorAndWeight.getWeight());
    }

    @Test
    public void testColorAndWeightsGetParamsWithWeight() {
        ColorSearchParams.ColorAndWeight colorAndWeight = new ColorSearchParams.ColorAndWeight("123123", 10);
        assertEquals("123123", colorAndWeight.getColor());
        assertEquals(10, colorAndWeight.getWeight());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testColorAndWeightsInvalidColor() {
        ColorSearchParams.ColorAndWeight colorAndWeight = new ColorSearchParams.ColorAndWeight("1234567");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testColorAndWeightsColorInvalidMinWeight() {
        ColorSearchParams.ColorAndWeight colorAndWeight = new ColorSearchParams.ColorAndWeight("123456", 0);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testColorAndWeightsColorInvalidMaxWeight() {
        ColorSearchParams.ColorAndWeight colorAndWeight = new ColorSearchParams.ColorAndWeight("123456", 101);
    }

    @Test
    public void testColorSearchToMap() {
        ColorSearchParams colorSearchParams = new ColorSearchParams("123456");
        Multimap<String, String> params = colorSearchParams.toMap();
        assertEquals("123456", params.get("color").toArray(new String[0])[0]);
    }

    @Test
    public void testColorAndWeightToMap() {
        ColorSearchParams.ColorAndWeight colorAndWeight1 = new ColorSearchParams.ColorAndWeight("000000", 10);
        ColorSearchParams.ColorAndWeight colorAndWeight2 = new ColorSearchParams.ColorAndWeight("ffffff", 20);
        ColorSearchParams colorSearchParams =
            new ColorSearchParams(Lists.newArrayList(colorAndWeight1, colorAndWeight2));
        Multimap<String, String> params = colorSearchParams.toMap();
        String[] colors = params.get("color").toArray(new String[0]);
        assertEquals(2, colors.length);
        assertEquals("000000:10", colors[0]);
        assertEquals("ffffff:20", colors[1]);
    }

    @Test
    public void testColorAndWeightWithSameColorToMap() {
        ColorSearchParams.ColorAndWeight colorAndWeight1 = new ColorSearchParams.ColorAndWeight("ffffff", 10);
        ColorSearchParams.ColorAndWeight colorAndWeight2 = new ColorSearchParams.ColorAndWeight("ffffff", 20);
        ColorSearchParams colorSearchParams =
            new ColorSearchParams(Lists.newArrayList(colorAndWeight1, colorAndWeight2));
        Multimap<String, String> params = colorSearchParams.toMap();
        String[] colors = params.get("color").toArray(new String[0]);
        assertEquals(2, colors.length);
        assertEquals("ffffff:10", colors[0]);
        assertEquals("ffffff:20", colors[1]);
    }

}
