package com.visenze.visearch;

import java.util.Map;

public class ImageResult {

    private final String imName;
    private final Map<String, String> metadata;
    private final Float score;

    public ImageResult(String imName, Map<String, String> metatdata, Float score) {
        this.imName = imName;
        this.metadata = metatdata;
        this.score = score;
    }

    public String getImName() {
        return imName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Float getScore() {
        return score;
    }

}
