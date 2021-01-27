package com.visenze.productsearch.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.common.util.ViJsonAny;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * <h1> Product Info Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 20 Jan 2021
 */
public class ProductTest {
    final ObjectMapper mapper = new ObjectMapper();
    final String JSON_KNOWN = "{\"product_id\":\"PRODUCT_ID\",\"main_image_url\":\"IMAGE_URL\",\"score\":0.827}";
    final String JSON_UNKNOWN = "{\"data\":{\"sku\":1234,\"product_name\":\"PRODUCT_NAME\",\"sale_price\":{\"currency\":\"SGD\",\"value\":\"120\"},\"color\":[\"blue\",\"red\",\"green\"]}}";

    /**
     * Combine two json format strings linearly
     *
     * @param first string that acts as head
     * @param second string that joins the first at the back
     * @return simple concat-ed string
     */
    private String concatJsonString(String first, String second) {
        if (first.endsWith("}") && second.startsWith("{")) {
            return first.substring(0, first.length() - 1) + "," + second.substring(1);
        }
        return null;
    }

    /**
     * Verifies known data fields - all members except 'data'. Checks are based
     * on values of jsonFormat
     *
     * @param info ProductInfo to verify
     */
    private void verifyKnownOnly(Product info) {
        assertEquals("PRODUCT_ID", info.getProductId());
        assertEquals("IMAGE_URL", info.getMainImageUrl());
        assertEquals(0.827, info.getScore(), 0.0001f);
    }

    /**
     * Verifies unknown data fields - only 'data' member. Checks are based on
     * values of jsonFormatData
     *
     * @param info ProductInfo to verify
     */
    private void verifyUnknownOnly(Product info) {
        Map<String, ViJsonAny> mappedData = info.getData();
        assertEquals(1234, (int)mappedData.get("sku").asInteger());
        assertEquals("PRODUCT_NAME", mappedData.get("product_name").asString());

        Map<String, String> price_map = mappedData.get("sale_price").asStringStringMap();
        assertEquals(2, price_map.size());
        assertEquals("SGD", price_map.get("currency"));
        assertEquals("120", price_map.get("value"));

        List<String> colors = mappedData.get("color").asStringList();
        assertEquals(3, colors.size());
        assertEquals("blue", colors.get(0));
        assertEquals("red", colors.get(1));
        assertEquals("green", colors.get(2));
    }

    @Test
    public void testJsonDeserializingKnown() {
        try {
            Product info = mapper.readValue(JSON_KNOWN, Product.class);
            verifyKnownOnly(info);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }

    @Test
    public void testJsonDeserializingUnknown() {
        try {
            Product info = mapper.readValue(JSON_UNKNOWN, Product.class);
            verifyUnknownOnly(info);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }

    @Test
    public void testJsonDeserializingSimulate() {
        final String simulatedResponse = concatJsonString(JSON_KNOWN, JSON_UNKNOWN);
        try {
            Product info = mapper.readValue(simulatedResponse, Product.class);
            verifyKnownOnly(info);
            verifyUnknownOnly(info);
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }
}