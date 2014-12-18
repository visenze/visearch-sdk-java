package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.FacetItem;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class FacetMixin {

    @JsonProperty("key")
    protected String key;

    @JsonProperty("items")
    protected List<FacetItem> facetItems;

}