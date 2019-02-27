package com.visenze.visearch;

import java.util.Map;

public class ImageResult {

    private final String imName;
    private final Map<String, String> metadata;
    private final Float score;
    private String s3Url;
    private Map<String, Object> vsMetadata;

    public ImageResult(String imName, Map<String, String> metadata, Float score) {
        this.imName = imName;
        this.metadata = metadata;
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


    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public Map<String, Object> getVsMetadata() {
        return vsMetadata;
    }

    public void setVsMetadata(Map<String, Object> vsMetadata) {
        this.vsMetadata = vsMetadata;
    }
}
