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

    private int itemCount;

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

    @JsonProperty("item_count")
    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
