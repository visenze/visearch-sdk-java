package com.visenze.productsearch.response;

import com.visenze.common.util.ViJsonAny;
import com.visenze.common.util.ViJsonMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * <h1> Object Product Result test </h1>
 * Test parsing of a sample response text into the class using deserialization
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 18 Mar 2021
 */
public class ObjectProductResultTest extends ViJsonMapper {
    final String RESPONSE = "{\n" +
            "   \"type\": \"top\",\n" +
            "   \"score\": 0.683,\n" +
            "   \"box\": [\n" +
            "       32,\n" +
            "       183,\n" +
            "       862,\n" +
            "       1062\n" +
            "   ],\n" +
            "   \"attributes\": {},\n" +
            "    \"total\": 985,\n" +
            "   \"result\": [\n" +
            "   {\n" +
            "       \"product_id\": \"POMELO2-AF-SG_b28d580ccf5dfd999d1006f15f773bb371542559\",\n" +
            "       \"main_image_url\": \"http://d3vhkxmeglg6u9.cloudfront.net/img/p/2/2/1/8/0/6/221806.jpg\",\n" +
            "       \"data\": {\n" +
            "           \"link\": \"https://iprice.sg/r/p/?_id=b28d580ccf5dfd999d1006f15f773bb371542559\",\n" +
            "           \"product_name\": \"Skrrrrt Cropped Graphic Hoodie Light Grey\",\n" +
            "           \"sale_price\": {\n" +
            "               \"currency\": \"SGD\",\n" +
            "               \"value\": \"44.0\"\n" +
            "           }\n" +
            "       }\n" +
            "   },\n" +
            "   {\n" +
            "       \"product_id\": \"POMELO2-AF-SG_43d7a0fb6e12d1f1079e6efb38b9d392fa135cdd\",\n" +
            "       \"main_image_url\": \"http://d3vhkxmeglg6u9.cloudfront.net/img/p/1/6/2/3/2/1/162321.jpg\",\n" +
            "       \"data\": {\n" +
            "           \"link\": \"https://iprice.sg/r/p/?_id=43d7a0fb6e12d1f1079e6efb38b9d392fa135cdd\",\n" +
            "           \"product_name\": \"Premium Twisted Cold Shoulder Top Pink\",\n" +
            "           \"sale_price\": {\n" +
            "               \"currency\": \"SGD\",\n" +
            "               \"value\": \"54.0\"\n" +
            "           }\n" +
            "       }\n" +
            "   }\n" +
            "   ]\n" +
            "}";

    @Test
    public void testParse() {
        try {
            ObjectProductResult result = mapper.readValue(RESPONSE, ObjectProductResult.class);

            assertEquals("top", result.getType());

            Float score = 0.683f;
            assertEquals(score, result.getScore());

            assertEquals(true, result.getBox().equals(Arrays.asList(32,183,862,1062)));

            assertEquals(true, result.getAttributes().isEmpty());

            Integer total = 985;
            assertEquals(total, result.getTotal());

            List<Product> products = result.getResult();
            assertEquals(2, products.size());

            assertEquals("POMELO2-AF-SG_b28d580ccf5dfd999d1006f15f773bb371542559", products.get(0).getProductId());

            assertEquals("http://d3vhkxmeglg6u9.cloudfront.net/img/p/2/2/1/8/0/6/221806.jpg", products.get(0).getMainImageUrl());

            Map<String, ViJsonAny> data = products.get(0).getData();
            assertEquals("https://iprice.sg/r/p/?_id=b28d580ccf5dfd999d1006f15f773bb371542559", data.get("link").asString());

            assertEquals("Skrrrrt Cropped Graphic Hoodie Light Grey", data.get("product_name").asString());

            Map<String,String> price = data.get("sale_price").asStringStringMap();
            assertEquals("SGD", price.get("currency"));
            assertEquals("44.0", price.get("value"));

            assertEquals("POMELO2-AF-SG_43d7a0fb6e12d1f1079e6efb38b9d392fa135cdd", products.get(1).getProductId());

            assertEquals("http://d3vhkxmeglg6u9.cloudfront.net/img/p/1/6/2/3/2/1/162321.jpg", products.get(1).getMainImageUrl());

            data = products.get(1).getData();
            assertEquals("https://iprice.sg/r/p/?_id=43d7a0fb6e12d1f1079e6efb38b9d392fa135cdd", data.get("link").asString());

            assertEquals("Premium Twisted Cold Shoulder Top Pink", data.get("product_name").asString());

            price = data.get("sale_price").asStringStringMap();
            assertEquals("SGD", price.get("currency"));
            assertEquals("54.0", price.get("value"));

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}