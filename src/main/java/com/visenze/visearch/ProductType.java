package com.visenze.visearch;


import java.util.List;

public class ProductType {

    private final String type;
    private final Float score;
    private final List<Integer> box;

    public ProductType(String type, Float score, List<Integer> box) {
        this.type = type;
        this.score = score;
        this.box = box;
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

    @Override
    public String toString() {
        return "ProductType{" +
                "type='" + type + '\'' +
                ", score=" + score +
                ", box=" + box +
                '}';
    }
}
