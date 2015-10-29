package com.visenze.visearch.internal;

import com.google.common.collect.Maps;
import java.util.Map;

public abstract class ResponseBase {
    private Map<String, String> headers = Maps.newHashMap();
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
}
