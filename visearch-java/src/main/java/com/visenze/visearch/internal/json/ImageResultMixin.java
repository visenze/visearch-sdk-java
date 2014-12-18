package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ImageResultMixin {

    @JsonProperty("value_map")
    protected Map<String, String> fields;

    @JsonProperty("score")
    protected Float score;

}
