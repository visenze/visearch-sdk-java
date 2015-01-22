package com.visenze.visearch;

public class Box {

    private Integer x1;

    private Integer x2;

    private Integer y1;

    private Integer y2;

    public Box() {
    }

    public Box(Integer x1, Integer x2, Integer y1, Integer y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Box setX1(Integer x1) {
        this.x1 = x1;
        return this;
    }

    public Box setX2(Integer x2) {
        this.x2 = x2;
        return this;
    }

    public Box setY1(Integer y1) {
        this.y1 = y1;
        return this;
    }

    public Box setY2(Integer y2) {
        this.y2 = y2;
        return this;
    }

    public Integer getX1() {
        return x1;
    }

    public Integer getX2() {
        return x2;
    }

    public Integer getY1() {
        return y1;
    }

    public Integer getY2() {
        return y2;
    }

    public boolean allCoordsExist() {
        return x1 != null && x2 != null && y1 != null && y2 != null;
    }

    public void scale(float ratio) {
        x1 = (int) (x1 * ratio);
        x2 = (int) (x2 * ratio);
        y1 = (int) (y1 * ratio);
        y2 = (int) (y2 * ratio);
    }

}
