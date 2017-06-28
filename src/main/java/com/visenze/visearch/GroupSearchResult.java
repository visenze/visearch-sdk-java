package com.visenze.visearch;

import java.util.List;

/**
 * Created by Hung on 28/6/17.
 *
 * Added grouped results for group_by parameter e.g. group by mpid
 *
 */
public class GroupSearchResult {

    private List<ImageResult> result;
    private String groupValue;

    public GroupSearchResult() {}

    public List<ImageResult> getResult() {
        return result;
    }

    public void setResult(List<ImageResult> result) {
        this.result = result;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }
}
