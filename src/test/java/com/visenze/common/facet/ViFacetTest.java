package com.visenze.common.facet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.common.exception.ViException;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

/**
 * <h1> Facet Unit Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 18 Jan 2021
 */
public class ViFacetTest extends TestCase {
    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * Dummy json formatted string of facet data
     */
    private static final String jsonFacet =
            "{" +
            "\"key\":\"this_is_some_facets_field_name\"," +
            "\"items\":[" +
                        "{\"value\":\"item_1\"}," +
                        "{\"value\":\"item_2\"}," +
                        "{\"value\":\"item_3\"}" +
                        "]," +
            "\"range\":{" +
                        "\"min\":50," +
                        "\"max\":100" +
                        "}" +
            "}";

    /**
     * Test the facet class deserializing. Everytime the facet updates its' own
     * data format / parameters, we can test using the jsonFacet as a base case.
     */
    @Test
    public void testFacet(){
        ViFacet facet;
        try{
            facet = mapper.readValue(jsonFacet, ViFacet.class);
        } catch(IOException e){
            throw new ViException(e.getMessage());
        }
        // verify
        assertEquals("this_is_some_facets_field_name",facet.getKey());
        assertEquals("item_1",facet.getFacetItems().get(0).getValue());
        assertEquals("item_2",facet.getFacetItems().get(1).getValue());
        assertEquals("item_3",facet.getFacetItems().get(2).getValue());
        assertEquals(50,facet.getRange().getMin());
        assertEquals(100,facet.getRange().getMax());
    }
}
