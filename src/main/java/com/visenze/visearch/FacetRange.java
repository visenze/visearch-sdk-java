package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <h1> Facet Range </h1>
 * Referencing from the dashboard - https://dashboard.visenze.com
 * 'Field' with 'Type' float or integer will use a range from lower to upper
 * bounds (min to max).
 *
 * @since 15 Jan 2021
 */
public class FacetRange {

    /**
     * The lower boundary numerical (float, double, integer, etc) value.
     */
    @JsonProperty("min")
    private Number min;

    /**
     * The upper boundary numerical (float, double, integer, etc) value.
     */
    @JsonProperty("max")
    private Number max;

    /**
     * Get the lower numerical boundary
     *
     * @return min
     */
    public Number getMin() {
        return min;
    }

    /**
     * Set the lower numerical boundary
     *
     * @param min Lower boundary
     */
    public void setMin(Number min) {
        this.min = min;
    }

    /**
     * Get the upper numerical boundary
     *
     * @return max
     */
    public Number getMax() {
        return max;
    }

    /**
     * Set the upper numerical boundary
     *
     * @param max Upper boundary
     */
    public void setMax(Number max) {
        this.max = max;
    }
}
