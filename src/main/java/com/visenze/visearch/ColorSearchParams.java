package com.visenze.visearch;

import com.google.common.collect.Multimap;

public class ColorSearchParams extends BaseSearchParams {

    private String color;

    public ColorSearchParams(String color) {
        super();
        this.color = color;
    }

    public ColorSearchParams setColor(String color) {
        this.color = color;
        return this;
    }

    public String getColor() {
        return color;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        map.put("color", color);
        return map;
    }
}
