package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageResult {

    private final String imName;
    private final Map<String, String> metadata;
    private final Float score;
    private Float rerankScore;
    private String s3Url;
    private Map<String, String> vsMetadata;
    private List<ImageResult> alternatives;
    private Map<String, Object> tags;
    private Boolean pinned;

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


    public Float getRerankScore() {
        return rerankScore;
    }

    public void setRerankScore(Float rerankScore) {
        this.rerankScore = rerankScore;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public Map<String, String> getVsMetadata() {
        return vsMetadata;
    }

    public void setVsMetadata(Map<String, String> vsMetadata) {
        this.vsMetadata = vsMetadata;
    }

    public List<ImageResult> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<ImageResult> alternatives) {
        this.alternatives = alternatives;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }
}
