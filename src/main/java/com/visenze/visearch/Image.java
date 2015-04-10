package com.visenze.visearch;

import java.util.HashMap;
import java.util.Map;

public class Image {

    private final String imName;
    private final String imUrl;
    private final Map<String, String> metadata;

    public Image(String imName, String imUrl) {
        this.imName = imName;
        this.imUrl = imUrl;
        this.metadata = new HashMap<String, String>();
    }

    public Image(String imName, String imUrl, Map<String, String> metadata) {
        this.imName = imName;
        this.imUrl = imUrl;
        this.metadata = metadata;
    }

    public String getImName() {
        return imName;
    }

    public String getImUrl() {
        return imUrl;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

}
