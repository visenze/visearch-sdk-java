package com.visenze.visearch;

public class FacetItem {

    private String value;

    private Integer count;

    public FacetItem(String value, Integer count) {
        this.value = value;
        this.count = count;
    }

    public String getValue() {
        return value;
    }

    public Integer getCount() {
        return count;
    }

}
