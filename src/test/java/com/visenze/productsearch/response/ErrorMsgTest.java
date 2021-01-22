package com.visenze.productsearch.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.*;

/**
 * <h1> Error Type Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 20 Jan 2021
 */
public class ErrorMsgTest {
    final String jsonFormat = "{\"code\": 100, \"message\": \"Parameter 'im_url' is required\"}";
    /**
     * Parsing JSON formatted string
     */
    @Test
    public void testJsonDeserializing() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            ErrorMsg err = mapper.readValue(jsonFormat, ErrorMsg.class);
            assertEquals(100, err.getCode().intValue());
            assertEquals("Parameter 'im_url' is required", err.getMessage());
        }
        catch (IOException e) {
            fail("Failed to let JSON auto-deserialize");
        }
    }
}