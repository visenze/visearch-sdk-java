package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ImageMixin {

    @JsonProperty("im_name")
    protected String imName;

    @JsonProperty("im_url")
    protected String imUrl;

}
