package com.visenze.visearch.internal;

import com.google.common.collect.Maps;
import java.util.Map;

public abstract class ResponseBase {

    private String errorMessage;

    private Throwable cause;

    private String rawResponseMessage;

    private Map<String, String> headers = Maps.newHashMap();

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setCause(Throwable e) {
        this.cause = e;
    }

    public Throwable getCause() {
        return this.cause;
    }

    public void setRawResponseMessage(String rawResponseMessage) {
        this.rawResponseMessage = rawResponseMessage;
    }

    public String getRawResponseMessage() {
        return this.rawResponseMessage;
    }
}
