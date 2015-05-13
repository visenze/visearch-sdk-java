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

    private void setImName(String imName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(imName), "im_name must not be null or empty for pre-indexed search.");
        this.imName = imName;
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
