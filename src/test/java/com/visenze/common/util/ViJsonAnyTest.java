package com.visenze.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.visearch.internal.InternalViSearchException;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * <h1> ViJsonAny Unit Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 20 Jan 2021
 */
public class ViJsonAnyTest extends TestCase {
    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * Dummy json formatted string data for arrays
     */
    private static final String jsonArrayInteger = "[0,20,10]";
    private static final String jsonArrayString = "[\"A\",\"B\",\"C\"]";
    private static final String jsonArrayMixed = "[0,\"B\",true]";
    private static final String jsonArrayIllegalNested = "[{\"MoreData\":\"Data1\",\"SomeMore\":\"Data2\"}]";
    /**
     * Dummy json formatted string data for maps
     */
    private static final String jsonMapString = "{\"key\":\"value\",\"more_key\":\"more_value\"}";
    private static final String jsonMapInteger = "{\"key\":123,\"456\":786}";
    private static final String jsonMapMixed = "{\"str_key\":\"str\",\"num_key\":123}";
    private static final String jsonMapIllegal = "{\"root_key\":{\"str_elem\":\"val\"},\"another_root\":{\"int_elem\":5}}";
    /**
     * Dummy json formatted string data for direct value
     */
    private static final String jsonValueString = "\"Hello\"";
    private static final String jsonValueInteger = "420";

    /**
     * Convert Json formatted string to a JsonNode object.
     *
     * @param jsonString string to convert
     * @return JsonNode based on string
     */
    private static JsonNode ToJsonObject(String jsonString) {
        try {
            return mapper.readTree(jsonString);
        } catch(IOException e) {
            throw new InternalViSearchException(e.getMessage());
        }
    }

    /**
     * Test arrays
     */
    @Test
    public void testArray() {
        tryArray(jsonArrayInteger);
        tryArray(jsonArrayString);
        tryArray(jsonArrayMixed);
        tryIllegalArray(jsonArrayIllegalNested);
    }

    /**
     * Test maps
     */
    @Test
    public void testMap() {
        tryMap(jsonMapString);
        tryMap(jsonMapInteger);
        tryMap(jsonMapMixed);
        tryIllegalMap(jsonMapIllegal);
    }

    /**
     * Test values
     */
    @Test
    public void testValue() {
        tryValue(jsonValueInteger);
        tryValue(jsonValueString);
    }

    /**
     * Test legal json array formats with conversion to String types
     *
     * @param jsonData json formatted string to test
     */
    private void tryArray(String jsonData) {
        ViJsonAny any = new ViJsonAny(ToJsonObject(jsonData));
        assertEquals(jsonData, any.getJsonNode().toString());
        assertEquals(Boolean.TRUE, any.isArray());
        assertEquals(Boolean.FALSE, any.isValue());
        assertEquals(Boolean.FALSE, any.isIllegal());
        // split element from string
        String temp = jsonData;
        temp = temp.replace("[", "");
        temp = temp.replace("]", "");
        temp = temp.replace("\"", "");
        List<String> checkList = new ArrayList<String>(Arrays.asList(temp.split(",")));
        List<String> list = any.getAsList(new TypeReference<List<String>>() {});
        // check each element
        assertEquals(checkList.size(), list.size());
        for (int i = 0; i < checkList.size(); ++i) {
            assertEquals(checkList.get(i), list.get(i));
        }
    }

    /**
     * Test illegal json array formats
     *
     * @param jsonData json formatted string to test
     */
    private void tryIllegalArray(String jsonData) {
        ViJsonAny any = new ViJsonAny(jsonData);
        assertEquals(jsonData, any.getJsonNode().toString());
        assertEquals(Boolean.TRUE, any.isArray());
        assertEquals(Boolean.TRUE, any.isIllegal());
    }

    /**
     * Test legal json map formats with conversion to String types
     *
     * @param jsonData json formatted string to test
     */
    private void tryMap(String jsonData) {
        ViJsonAny any = new ViJsonAny(jsonData);
        assertEquals(jsonData, any.getJsonNode().toString());
        assertEquals(Boolean.FALSE, any.isArray());
        assertEquals(Boolean.FALSE, any.isValue());
        assertEquals(Boolean.FALSE, any.isIllegal());
        // split element from string
        String temp = jsonData;
        temp = temp.replace("{", "");
        temp = temp.replace("}", "");
        temp = temp.replace("\"", "");
        Map<String, String> checkMap = new HashMap<String, String>();
        List<String> commaSep = new ArrayList<String>(Arrays.asList(temp.split(",")));
        for (String s : commaSep) {
            List<String> pairs = new ArrayList<String>(Arrays.asList(s.split(":")));
            checkMap.put(pairs.get(0), pairs.get(1));
        }
        Map<String, String> map = any.getAsMap(new TypeReference<Map<String, String>>(){});
        assertEquals(checkMap.size(), map.size());
        // check each element
        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertEquals(checkMap.get(entry.getKey()), entry.getValue());
        }
    }

    /**
     * Test illegal json map formats
     *
     * @param jsonData json formatted string to test
     */
    private void tryIllegalMap(String jsonData) {
        ViJsonAny any = new ViJsonAny(ToJsonObject(jsonData));
        assertEquals(jsonData, any.getJsonNode().toString());
        assertEquals(Boolean.FALSE, any.isArray());
        assertEquals(Boolean.FALSE, any.isValue());
        assertEquals(Boolean.TRUE, any.isIllegal());
    }

    /**
     * Test legal json value formats with conversion to String types
     *
     * @param jsonData json formatted string to test
     */
    private void tryValue(String jsonData) {
        ViJsonAny any = new ViJsonAny(jsonData);
        assertEquals(jsonData, any.getJsonNode().toString());
        assertEquals(Boolean.FALSE, any.isArray());
        assertEquals(Boolean.TRUE, any.isValue());
        assertEquals(Boolean.FALSE, any.isIllegal());
        // split element from string
        String temp = jsonData;
        temp = temp.replace("{", "");
        temp = temp.replace("}", "");
        temp = temp.replace("[", "");
        temp = temp.replace("]", "");
        temp = temp.replace("\"", "");
        // verify value
        String val = any.getAsValue(new TypeReference<String>(){});
        assertEquals(temp, val);
    }

}