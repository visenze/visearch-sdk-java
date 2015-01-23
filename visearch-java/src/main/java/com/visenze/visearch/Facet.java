package com.visenze.visearch;

import java.util.List;

public class Facet {

    private String key;

    private List<FacetItem> facetItems;

    public Facet(String key, List<FacetItem> facetItems) {
        this.key = key;
        this.facetItems = facetItems;
    }

    public String getKey() {
        return key;
    }

    public List<FacetItem> getFacetItems() {
        return facetItems;
    }

}
