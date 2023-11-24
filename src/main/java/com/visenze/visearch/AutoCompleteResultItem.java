package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hung on 23/11/23.
 */
public class AutoCompleteResultItem {
    private String text;
    private double score;

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("score")
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
