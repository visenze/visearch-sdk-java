package com.visenze.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.visearch.internal.InternalViSearchException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <h1> ViJsonAny Unit Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 20 Jan 2021
 */
public class ViJsonAnyTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * Dummy json formatted string data for arrays
     */
    private static final String jsonArrayInteger = "[0,20,10]";
    private static final String jsonArrayStringInteger = "[\"0\",\"20\",\"10\"]";
    private static final String jsonArrayString = "[\"A\",\"B\",\"C\"]";
    /**
     * Dummy json formatted string data for maps
     */
    private static final String jsonMapString = "{\"key\":\"value\",\"more_key\":\"more_value\"}";
    private static final String jsonMapInteger = "{\"key\":123,\"456\":786}";
    private static final String jsonMapStringInteger = "{\"key\":\"123\",\"456\":\"786\"}";
    /**
     * Dummy json formatted string data for direct value
     */
    private static final String jsonValueString = "\"Hello\"";
    private static final String jsonValueInteger = "420";
    private static final String jsonValueDouble = "0.023";

    @Test
    public void TestValue() {
        ViJsonAny stringData = new ViJsonAny(jsonValueString);
        ViJsonAny integerData = new ViJsonAny(jsonValueInteger);
        ViJsonAny doubleData = new ViJsonAny(jsonValueDouble);

        // validate converting a value type into a container type
        assertNull(stringData.asStringList());
        assertNull(stringData.asStringStringMap());
        assertNull(integerData.asStringList());
        assertNull(integerData.asStringStringMap());
        assertNull(doubleData.asStringList());
        assertNull(doubleData.asStringStringMap());
        // validate loading unloaded data as the right format
        assertEquals("Hello", stringData.asString());
        assertEquals(420, (int)integerData.asInteger());
        assertEquals(0.023, doubleData.asDouble(), 0.0001);
        // validate loading already loaded data as a different format
        assertNull(stringData.asInteger());
        assertNull(integerData.asDouble());
        assertNull(doubleData.asString());

        // reset testing variables
        stringData = new ViJsonAny(jsonValueString);
        integerData = new ViJsonAny(jsonValueInteger);
        doubleData = new ViJsonAny(jsonValueDouble);

        // validate loading unloaded data as the wrong format
        try{
            // special case for trying to load string as any other type due to
            // conversion issues, unlike double->integer, string->integer is not
            // that straightforward
            stringData.asInteger();
            fail();
        } catch (InternalViSearchException e) {
            // due to strings not being able to convert to integer, the catch
            // would leave the ViJsonAny to be 'unloaded', so loading as the
            // right type can still work
            assertEquals("Hello", stringData.asString());
        }
        assertEquals(420.0, integerData.asDouble(), 0.0001);
        assertEquals("0.023", doubleData.asString());
        // validate loading already loaded data as the right format
        assertNull(integerData.asInteger());
        assertNull(doubleData.asDouble());
    }

    @Test
    public void TestList() {
        ViJsonAny stringListData = new ViJsonAny(jsonArrayString);
        ViJsonAny integerListData = new ViJsonAny(jsonArrayInteger);

        // validate converting a container type into a value/map type
        assertNull(stringListData.asString());
        assertNull(integerListData.asInteger());
        assertNull(stringListData.asStringStringMap());
        assertNull(integerListData.asStringIntegerMap());
        // validate loading unloaded data as the right format
        try {
            assertEquals(jsonArrayString,  mapper.writeValueAsString(stringListData.asStringList()));
        } catch (JsonProcessingException e) {
            fail();
        }
        try {
            assertEquals(jsonArrayInteger,  mapper.writeValueAsString(integerListData.asIntegerList()));
        } catch (JsonProcessingException e) {
            fail();
        }
        // validate loading already loaded data as a different format
        assertNull(stringListData.asDoubleList());
        assertNull(integerListData.asStringList());

        // reset testing variables
        stringListData = new ViJsonAny(jsonArrayString);
        integerListData = new ViJsonAny(jsonArrayInteger);

        // validate loading unloaded data as the wrong format
        try {
            assertEquals(jsonArrayString,  mapper.writeValueAsString(stringListData.asIntegerList()));
            fail();
        } catch (InternalViSearchException internalError) {
            // needs to fall in here for convert list<string> to list<any other wrong>
            // do nothing - its a pass
        } catch (JsonProcessingException jsonError) {
            // InternalViSearchException should have triggered exception before
            // mapper.writeValueAsString gets called
            fail();
        }

        try {
            assertEquals(jsonArrayStringInteger,  mapper.writeValueAsString(integerListData.asStringList()));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void TestMap() {
        ViJsonAny mapStringStringData = new ViJsonAny(jsonMapString);
        ViJsonAny mapStringIntegerData = new ViJsonAny(jsonMapInteger);

        // validate unloaded data loading into wrong format
        assertNull(mapStringStringData.asStringList());
        assertNull(mapStringStringData.asInteger());
        assertNull(mapStringIntegerData.asStringList());
        assertNull(mapStringIntegerData.asInteger());
        // validate loading unloaded data into the right format
        try {
            assertEquals(jsonMapString, mapper.writeValueAsString(mapStringStringData.asStringStringMap()));
        } catch (JsonProcessingException e) {
            fail();
        }
        try {
            assertEquals(jsonMapInteger, mapper.writeValueAsString(mapStringIntegerData.asStringIntegerMap()));
        } catch (JsonProcessingException e) {
            fail();
        }
        // validate loading already loaded data as a different format
        assertNull(mapStringStringData.asStringIntegerMap());
        assertNull(mapStringIntegerData.asStringStringMap());

        // reset testing variables
        mapStringStringData = new ViJsonAny(jsonMapString);
        mapStringIntegerData = new ViJsonAny(jsonMapInteger);

        // validate loading unloaded data into the wrong format
        try {
            assertEquals(jsonMapString, mapper.writeValueAsString(mapStringStringData.asStringIntegerMap()));
            fail();
        } catch (InternalViSearchException internalError) {
            // needs to fall in here for convert Map<string,string> to Map<string, any other wrong type>
            // do nothing - its a pass
        } catch (JsonProcessingException jsonError) {
            // InternalViSearchException should have triggered exception before
            // mapper.writeValueAsString gets called
            fail();
        }

        try {
            assertEquals(jsonMapStringInteger, mapper.writeValueAsString(mapStringIntegerData.asStringStringMap()));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

}