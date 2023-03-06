package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hung on 3/3/23.
 */
@JsonPropertyOrder({"type", "url", "index"})
public class BestImage implements Serializable {

    private ImageType type = ImageType.PRODUCT;
    private String url;
    private String index;

    @JsonProperty("type")
    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("index")
    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public enum ImageType {
        PRODUCT("product"),
        OUTFIT("outfit");

        private static final Map<String, ImageType> VALUE_MAP = new HashMap<String, ImageType>();

        static {
            ImageType[] validValues = ImageType.values();
            for(ImageType value: validValues) {
                VALUE_MAP.put(value.getImageType(), value);
            }
        }


        private final String imageType;

        ImageType(String imageType) {
            this.imageType = imageType;
        }

        @JsonValue
        public String getImageType() {
            return imageType;
        }

        @JsonCreator
        public static ImageType fromName(String imageType) {
            return VALUE_MAP.get(imageType);
        }
    }
}