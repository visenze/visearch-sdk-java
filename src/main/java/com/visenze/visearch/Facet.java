package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Facet {

    private String key;

    @JsonProperty("items")
    private List<FacetItem> facetItems;

    private FacetRange range;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<FacetItem> getFacetItems() {
        return facetItems;
    }

    public void setFacetItems(List<FacetItem> facetItems) {
        this.facetItems = facetItems;
    }

    public FacetRange getRange() {
        return range;
    }

    public void setRange(FacetRange range) {
        this.range = range;
    }
}
