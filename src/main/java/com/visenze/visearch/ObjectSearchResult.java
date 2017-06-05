package com.visenze.visearch;

import java.util.List;
import java.util.Map;

/**
 * Created by dejun on 5/6/17.
 */
public class ObjectSearchResult {
    private String type;
    private Float score;
    private List<Integer> box;
    private Map<String, List<String>> attributes;
    private Map<String, List<String>> attributesList;
    private int total;
    private List<ImageResult> result;


    public ObjectSearchResult() {

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ImageResult> getResult() {
        return result;
    }

    public void setResult(List<ImageResult> result) {
        this.result = result;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public void setBox(List<Integer> box) {
        this.box = box;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public void setAttributesList(Map<String, List<String>> attributesList) {
        this.attributesList = attributesList;
    }
}
