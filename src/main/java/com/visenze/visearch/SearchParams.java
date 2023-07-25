package com.visenze.visearch;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;

public class SearchParams extends BaseSearchParams<SearchParams> {

    private String imName;

    public SearchParams(String imName) {
        super();
        setImName(imName);
    }

    private SearchParams setImName(String imName) {
        this.imName = imName;
        return this;
    }

    public String getImName() {
        return imName;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (imName != null) {
            map.put("im_name", imName);
        }
        return map;
    }
}
