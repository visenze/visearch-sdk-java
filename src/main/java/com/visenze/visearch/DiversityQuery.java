package com.visenze.visearch;

/**
 * Created by Hung on 18/2/20.
 */
public class DiversityQuery {
    private String field;
    private String value;
    private int ratio;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public String toParamValue() {
        return String.format("%s:%s:%s", field, value, ratio);
    }
}
