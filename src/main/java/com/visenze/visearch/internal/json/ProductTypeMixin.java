package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ProductTypeMixin {

    private final String type;
    private final Float score;
    private final List<Integer> box;

    public ProductTypeMixin(@JsonProperty("type") String type,
                            @JsonProperty("score") Float score,
                            @JsonProperty("box") List<Integer> box) {
        this.type = type;
        this.score = score;
        this.box = box;
    }

}
