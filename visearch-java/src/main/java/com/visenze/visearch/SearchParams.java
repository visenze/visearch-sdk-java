package com.visenze.visearch;

import com.google.common.collect.Multimap;

public class SearchParams extends BaseSearchParams {

    private String imName;

    public SearchParams(String imName) {
        super();
        this.imName = imName;
    }

    public SearchParams setImName(String imName) {
        this.imName = imName;
        return this;
    }

    public String getImName() {
        return imName;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        map.put("im_name", imName);
        return map;
    }
}
