package com.visenze.visearch;

import com.google.common.collect.Multimap;

import java.io.File;
import java.io.InputStream;

public class UploadSearchParams extends BaseSearchParams {

    private File imageFile;

    private InputStream imageStream;

    private Box box;

    private String imageUrl;

    public UploadSearchParams(File imageFile) {
        super();
        this.imageFile = imageFile;
    }

    public UploadSearchParams(InputStream imageStream) {
        super();
        this.imageStream = imageStream;
    }

    public UploadSearchParams(String imageUrl) {
        super();
        this.imageUrl = imageUrl;
    }

    public UploadSearchParams setBox(Box box) {
        this.box = box;
        return this;
    }

    public Box getBox() {
        return this.box;
    }

    public UploadSearchParams setImageFile(File imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public File getImageFile() {
        return imageFile;
    }

    public UploadSearchParams setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
        return this;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (box != null && box.allCoordsExist()) {
            map.put("box", box.getX1() + "," + box.getY1() + "," + box.getX2() + "," + box.getY2());
        }
        if (imageUrl != null) {
            map.put("im_url", imageUrl);
        }
        return map;
    }

}
