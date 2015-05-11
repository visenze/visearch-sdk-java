package com.visenze.visearch;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;

public class ColorSearchParams extends BaseSearchParams<ColorSearchParams> {

    private String color;

    public ColorSearchParams(String color) {
        super();
        setColor(color);
    }

    public ColorSearchParams setColor(String color) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(color), "color must not be null or empty.");
        Preconditions.checkArgument(color.matches("^[0-9a-fA-F]{6}$"), "Invalid color. " +
                "It should be a six hexadecimal number color code e.g. 123ACF.");
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
