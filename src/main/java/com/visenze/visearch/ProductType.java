package com.visenze.visearch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductType {

    private String type;
    private Float score;
    private List<Integer> box;
    private Map<String, List<String>> attributes;
    private Map<String, List<String>> attributesList;

    public ProductType() {}

    public ProductType(String type, Float score, List<Integer> box,
                       Map<String, List<String>> attributes, Map<String, List<String>> attributesList) {
        this.type = type;
        this.score = score;
        this.box = box;
        this.attributes = attributes;
        this.attributesList = attributesList;
    }

    public String getType() {
        return type;
    }

    public Float getScore() {
        return score;
    }

    public List<Integer> getBox() {
        return box;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public Map<String, List<String>> getAttributesList() {
        return attributesList;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "type='" + type + '\'' +
                ", score=" + score +
                ", box=" + box +
                ", attributes=" + attributes +
                ", attributesList=" + attributesList +
                '}';
    }
}
