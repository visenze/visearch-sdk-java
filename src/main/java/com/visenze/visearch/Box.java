package com.visenze.visearch;

import java.util.List;

public class Box {

    private final Integer x1;
    private final Integer x2;
    private final Integer y1;
    private final Integer y2;

    public Box(Integer x1, Integer y1, Integer x2, Integer y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Box(List<Integer> box) {
        this.x1 = box.get(0);
        this.y1 = box.get(1);
        this.x2 = box.get(2);
        this.y2 = box.get(3);
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

}
