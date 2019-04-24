package com.visenze.visearch;

import com.google.common.collect.Multimap;

/**
 * Created by zhumingyuan on 2019-04-24
 */
public class MatchSearchParams extends SearchParams {

    private static final int DEFAULT_OBJECT_LIMIT = -1;

    private int objectLimit = DEFAULT_OBJECT_LIMIT;

    public MatchSearchParams(String imName) {
        super(imName);
    }

    public int getObjectLimit() {
        return objectLimit;
    }

    public void setObjectLimit(int objectLimit) {
        this.objectLimit = objectLimit;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        map.put("object_limit", String.valueOf(objectLimit));
        return map;
    }
}
