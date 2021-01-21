package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <h1> Facet Item </h1>
 * Referencing from the dashboard - https://dashboard.visenze.com
 * 'Field' with 'Type' string will be split into a list of strings via comma
 * delimiter. Each substring generated this way will be a single ViFacetItem.
 *
 * @since 15 Jan 2021
 */
public class FacetItem {
    /**
     * The substring split(ed) from the 'Field' string
     */
    @JsonProperty("value")
    private String value;

    private Integer count;

    /**
     * Get the substring value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    public Integer getCount() {
        return count;
    }

    /**
     * Set the substring value
     *
     * @param value string of the desired facet value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
