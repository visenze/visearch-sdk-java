package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class FacetItemMixin {

    protected String key;
    protected Integer count;

    public FacetItemMixin(@JsonProperty("value") String value,
                          @JsonProperty("count") Integer count) {
        this.key = value;
        this.count = count;
    }

}
