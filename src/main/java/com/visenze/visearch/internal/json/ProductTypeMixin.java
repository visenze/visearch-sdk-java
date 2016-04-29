package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ProductTypeMixin {

    private final String type;
    private final Float score;
    private final List<Integer> box;
    private final Map<String, List<String>> attributes;
    private final Map<String, List<String>> attributesList;

    public ProductTypeMixin(@JsonProperty("type") String type,
                            @JsonProperty("score") Float score,
                            @JsonProperty("box") List<Integer> box,
                            @JsonProperty("attributes") Map<String, List<String>> attributes,
                            @JsonProperty("attributes_list") Map<String, List<String>> attributesList) {
        this.type = type;
        this.score = score;
        this.box = box;
        this.attributes = attributes;
        this.attributesList = attributesList;
    }

}
