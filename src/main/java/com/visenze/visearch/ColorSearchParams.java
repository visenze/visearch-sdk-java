package com.visenze.visearch;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.regex.Pattern;

public class ColorSearchParams extends BaseSearchParams<ColorSearchParams> {

    private static final Pattern COLOR_PATTERN = Pattern.compile("^[0-9a-fA-F]{6}$");

    private String color;
    private List<ColorAndWeight> colors;
    public ColorSearchParams(String color) {
        super();
        setColor(color);
    }

    public ColorSearchParams(List<ColorAndWeight> colorAndWeights) {
        super();
        setColors(colorAndWeights);
    }

    private void setColor(String color) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(color), "color must not be null or empty.");
        Preconditions.checkArgument(COLOR_PATTERN.matcher(color).matches(), "Invalid color. " +
                "It should be a six hexadecimal number color code e.g. 123ACF.");
        this.color = color;
    }

    private void setColors(List<ColorAndWeight> colors) {
        Preconditions.checkArgument(colors != null && !colors.isEmpty(), "colors must not be null or empty.");
        this.colors = colors;
    }

    public String getColor() {
        return color;
    }

    public List<ColorAndWeight> getColors() {
        return colors;
    }
    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (color != null) {
            map.put("color", color);
        } else if (colors != null) {
            for (ColorAndWeight color : colors) {
                map.put("colors", color.generateParam());
            }
        }
        return map;
    }

    public static class ColorAndWeight {

        private static final String COLOR_WEIGHT_JOINER = ":";
        private static final int MIN_WEIGHT = 1;
        private static final int MAX_WEIGHT = 100;

        private String color;
        private int weight;

        public ColorAndWeight(String color) {
            setColor(color);
        }

        public ColorAndWeight(String color, int weight) {
            setColor(color);
            setWeight(weight);
        }

        public String getColor() {
            return color;
        }

        public int getWeight() {
            return weight;
        }

        private void setColor(String color) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(color), "color must not be null or empty.");
            Preconditions.checkArgument(COLOR_PATTERN.matcher(color).matches(), "Invalid color. " +
                "It should be a six hexadecimal number color code e.g. 123ACF.");
            this.color = color;
        }

        private void setWeight(int weight) {
            Preconditions.checkArgument(MIN_WEIGHT <= weight && weight <= MAX_WEIGHT, "Invalid weight. " +
                "It should be in the range [1, 100].");
            this.weight = weight;
        }

        private String generateParam() {
            if (weight > 0) {
                return color + COLOR_WEIGHT_JOINER + weight;
            }
            return color;
        }
    }
}
