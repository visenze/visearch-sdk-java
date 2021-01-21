package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <h1> Facet </h1>
 * Each facet is identifiable through their key - Facet keys represents the
 * 'Field' names in https://dashboard.visenze.com that has the 'Searchable'
 * checkbox ticked. For the 'Field' with 'Type' such as float and integer will
 * result in using ViFacetRange to store the lower and upper bound (min, max).
 * For 'Field' with 'Type' string, a list of ViFacetItem will be used instead
 * - with each ViFacetItem identified via the comma delimiter in the string.
 * More about the facet parameter can be found at http://developers.visenze.com.
 *
 * @since 15 Jan 2021
 */
public class Facet {

    /**
     * Type of facet this is belong to. i.e. price, color, brand
     */
    @JsonProperty("key")
    private String key;

    /**
     * List of ViFacetItem
     */
    @JsonProperty("items")
    private List<FacetItem> facetItems;

    /**
     * ViFacet range
     */
    @JsonProperty("range")
    private FacetRange range;

    /**
     * Get this facet's key identifying what kind of facet this is
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Set this facet's key
     *
     * @param key What kind of facet is this
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Get the list of facet items
     *
     * @return facetItems
     */
    public List<FacetItem> getFacetItems() {
        return facetItems;
    }

    /**
     * Set the list of facet items
     *
     * @param facetItems The list of facet items for this facet
     */
    public void setFacetItems(List<FacetItem> facetItems) {
        this.facetItems = facetItems;
    }

    /**
     * Get the facet's range boundaries
     *
     * @return range
     */
    public FacetRange getRange() {
        return range;
    }

    /**
     * Set the facet's range boundaries
     *
     * @param range Range boundaries
     */
    public void setRange(FacetRange range) {
        this.range = range;
    }
}
