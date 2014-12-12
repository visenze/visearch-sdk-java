package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class FacetItemMixin {

    @JsonProperty("value")
    protected String key;

    @JsonProperty("count")
    protected Integer count;

}
