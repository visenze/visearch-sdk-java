package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Hung on 28/6/17.
 *
 * Added grouped results for group_by parameter e.g. group by mpid
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupSearchResult {

    private List<ImageResult> result;
    private String groupByValue;

    public GroupSearchResult() {}

    public GroupSearchResult(String groupValue, List<ImageResult> result) {
        this.result = result;
        this.groupByValue = groupValue;
    }

    public List<ImageResult> getResult() {
        return result;
    }

    public void setResult(List<ImageResult> result) {
        this.result = result;
    }

    public String getGroupByValue() {
        return groupByValue;
    }

    public void setGroupByValue(String groupByValue) {
        this.groupByValue = groupByValue;
    }
}
