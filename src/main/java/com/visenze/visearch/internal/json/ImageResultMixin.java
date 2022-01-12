package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.ImageResult;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ImageResultMixin {

    protected String imName;
    protected Map<String, Object> metadata;
    protected Float score;

    @JsonProperty("rerank_score")
    protected Float rerankScore;

    @JsonProperty("s3_url")
    protected String s3Url;

    @JsonProperty("vs_value_map")
    protected Map<String, Object> vsMetadata;

    @JsonProperty("alternatives")
    protected List<ImageResult> alternatives;

    @JsonProperty("tags")
    protected Map<String, Object> tags;


    public ImageResultMixin(@JsonProperty("im_name") String imName,
                            @JsonProperty("value_map") Map<String, Object> metadata,
                            @JsonProperty("score") Float score
                            ) {
        this.imName = imName;
        this.metadata = metadata;
        this.score = score;
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

    public Float getRerankScore() {
        return rerankScore;
    }

    public void setRerankScore(Float rerankScore) {
        this.rerankScore = rerankScore;
    }
}
