package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hung on 5/9/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetInfo {

    private String setId;

    private double setScore;

    @JsonProperty("set_id")
    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    @JsonProperty("set_score")
    public double getSetScore() {
        return setScore;
    }

    public void setSetScore(double setScore) {
        this.setScore = setScore;
    }
}
