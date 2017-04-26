package com.visenze.visearch;

import java.util.List;

public class GroupImageResult {

    private List<ImageResult> imageResultList;

    public GroupImageResult(List<ImageResult> imageResultList) {
        this.imageResultList = imageResultList;
    }

    public List<ImageResult> getImageResultList() {
        return imageResultList;
    }
}
