package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.FacetItem;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class FacetMixin {

    protected String key;
    protected List<FacetItem> facetItems;

    public FacetMixin(@JsonProperty("key") String key,
                      @JsonProperty("items") List<FacetItem> facetItems) {
        this.key = key;
        this.facetItems = facetItems;
    }

}