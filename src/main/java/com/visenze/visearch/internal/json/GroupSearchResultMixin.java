package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.ImageResult;

import java.util.List;

/**
 * Created by Hung on 28/6/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class GroupSearchResultMixin {
    protected List<ImageResult> result;
    protected String groupByValue;

    public GroupSearchResultMixin(@JsonProperty("group_by_value") String groupByValue , @JsonProperty("result") List<ImageResult> result) {
        this.result = result;
        this.groupByValue = groupByValue;
    }
}
