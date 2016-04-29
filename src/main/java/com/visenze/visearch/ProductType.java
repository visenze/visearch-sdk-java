package com.visenze.visearch;


import java.util.List;
import java.util.Map;

public class ProductType {

    private final String type;
    private final Float score;
    private final List<Integer> box;
    private final Map<String, List<String>> attributes;
    private final Map<String, List<String>> attributesList;

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
