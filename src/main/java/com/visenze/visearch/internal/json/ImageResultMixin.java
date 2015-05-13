package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ImageResultMixin {

    protected String imName;
    protected Map<String, Object> metadata;
    protected Float score;

    public ImageResultMixin(@JsonProperty("im_name") String imName,
                            @JsonProperty("value_map") Map<String, Object> metadata,
                            @JsonProperty("score") Float score) {
        this.imName = imName;
        this.metadata = metadata;
        this.score = score;
    }

}
