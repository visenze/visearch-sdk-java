package com.visenze.common.facet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <h1> Facet Item </h1>
 * Referencing from the dashboard - https://dashboard.visenze.com
 * 'Field' with 'Type' string will be split into a list of strings via comma
 * delimiter. Each substring generated this way will be a single ViFacetItem.
 * <p>
 * This class aims to be Json compatible by implementing Jackson annotation
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 15 Jan 2021
 * @see ViFacet
 */
public class ViFacetItem {
    /**
     * The substring split(ed) from the 'Field' string
     */
    @JsonProperty("value")
    private String value;

    /**
     * Get the substring value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the substring value
     *
     * @param value string of the desired facet value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
