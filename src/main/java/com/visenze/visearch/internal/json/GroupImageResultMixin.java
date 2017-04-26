package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.ImageResult;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class GroupImageResultMixin {

    protected List<ImageResult> imageResultList;

    public GroupImageResultMixin(@JsonProperty("group_result") List<ImageResult> imageResultList) {
        this.imageResultList = imageResultList;
    }

}
