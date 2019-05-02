package com.visenze.visearch;

import com.google.common.collect.Multimap;

/**
 * Created by zhumingyuan on 2019-04-24
 */
public class MatchSearchParams extends SearchParams {

    private static final int DEFAULT_OBJECT_LIMIT = -1;
    private static final int DEFAULT_RESULT_LIMIT = 10;

    private int objectLimit = DEFAULT_OBJECT_LIMIT;
    private int resultLimit = DEFAULT_RESULT_LIMIT;

    public MatchSearchParams(String imName) {
        super(imName);
    }

    public int getObjectLimit() {
        return objectLimit;
    }

    public MatchSearchParams setObjectLimit(int objectLimit) {
        this.objectLimit = objectLimit;
        return this;
    }

    public int getResultLimit() {
        return resultLimit;
    }

    public MatchSearchParams setResultLimit(int resultLimit) {
        this.resultLimit = resultLimit;
        return this;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        map.put("object_limit", String.valueOf(objectLimit));
        map.put("result_limit", String.valueOf(resultLimit));
        return map;
    }
}
