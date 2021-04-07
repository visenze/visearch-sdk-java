package com.visenze.productsearch.response;

import com.visenze.common.util.ViJsonAny;
import com.visenze.common.util.ViJsonMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class GroupProductResultTest extends ViJsonMapper {

    final String RESPONSE = "{\n" +
            "   \"group_by_value\": \"Wakingbee\",\n" +
            "   \"result\": [\n" +
            "   {\n" +
            "       \"product_id\": \"POMELO2-AF-SG_c22feb13f6956d09ce07a236c587e29fca2638cb\",\n" +
            "       \"main_image_url\": \"http://d3vhkxmeglg6u9.cloudfront.net/img/p/2/2/3/3/7/4/223374.jpg\",\n" +
            "       \"data\": {\n" +
            "           \"link\": \"https://iprice.sg/r/p/?_id=c22feb13f6956d09ce07a236c587e29fca2638cb\",\n" +
            "           \"product_name\": \"P.E. Shorts Pink\",\n" +
            "           \"sale_price\": {\n" +
            "               \"currency\": \"SGD\",\n" +
            "               \"value\": \"69.0\"\n" +
            "           }\n" +
            "       }\n" +
            "   },\n" +
            "   {\n" +
            "       \"product_id\": \"POMELO2-AF-SG_dec40b1909a774f125bae9b7707884414199aaa3\",\n" +
            "       \"main_image_url\": \"http://d3vhkxmeglg6u9.cloudfront.net/img/p/2/1/7/3/6/4/217364.jpg\",\n" +
            "       \"data\": {\n" +
            "           \"link\": \"https://iprice.sg/r/p/?_id=dec40b1909a774f125bae9b7707884414199aaa3\",\n" +
            "           \"product_name\": \"Waking Bee Sports Bra Aqua Blue\",\n" +
            "           \"sale_price\": {\n" +
            "               \"currency\": \"SGD\",\n" +
            "               \"value\": \"59.0\"\n" +
            "           }\n" +
            "       }\n" +
            "   }\n" +
            "   ]\n" +
            "}";

    @Test
    public void testParse() {

        try {
            GroupProductResult groupResult = mapper.readValue(RESPONSE, GroupProductResult.class);

            assertEquals("Wakingbee",groupResult.getGroupByValue());

            List<Product> results = groupResult.getProducts();
            assertEquals(2, results.size());

            assertEquals("POMELO2-AF-SG_dec40b1909a774f125bae9b7707884414199aaa3", results.get(1).getProductId());

            assertEquals("http://d3vhkxmeglg6u9.cloudfront.net/img/p/2/1/7/3/6/4/217364.jpg", results.get(1).getMainImageUrl());

            Map<String, ViJsonAny> data = results.get(1).getData();
            assertEquals("https://iprice.sg/r/p/?_id=dec40b1909a774f125bae9b7707884414199aaa3", data.get("link").asString());

            assertEquals("Waking Bee Sports Bra Aqua Blue", data.get("product_name").asString());

            Map<String,String> price = data.get("sale_price").asStringStringMap();
            assertEquals("SGD", price.get("currency"));
            assertEquals("59.0", price.get("value"));

        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

}