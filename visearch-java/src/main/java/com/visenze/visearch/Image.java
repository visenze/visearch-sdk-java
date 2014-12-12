package com.visenze.visearch;

import java.util.HashMap;
import java.util.Map;

public class Image {

    protected String imName;

    protected String imUrl;

    protected Map<String, String> fields;

    public Image() {
        this.fields = new HashMap<String, String>();
    }

    public Image(String imName, String imUrl, Map<String, String> fields) {
        this.imName = imName;
        this.imUrl = imUrl;
        this.fields = fields;
    }

    public String getImName() {
        return imName;
    }

    public String getImUrl() {
        return imUrl;
    }

    public Map<String, String> getFields() {
        return fields;
    }

}
