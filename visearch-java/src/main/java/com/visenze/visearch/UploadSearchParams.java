package com.visenze.visearch;

import com.google.common.collect.Multimap;

import java.io.File;

public class UploadSearchParams extends BaseSearchParams {

    private File imageFile;

    private byte[] imageBytes;

    private Box box;

    private String imageUrl;

    public UploadSearchParams(File imageFile) {
        super();
        this.imageFile = imageFile;
    }

    public UploadSearchParams(String imageUrl) {
        super();
        this.imageUrl = imageUrl;
    }

    public UploadSearchParams(byte[] imageBytes) {
        super();
        this.imageBytes = imageBytes;
    }

    public UploadSearchParams setBox(Box box) {
        this.box = box;
        return this;
    }

    public UploadSearchParams setImageFile(File imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public UploadSearchParams setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
        return this;
    }

    public Box getBox() {
        return this.box;
    }

    public File getImageFile() {
        return imageFile;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (box != null) {
            if (box.getX1() != null && box.getX2() != null &&
                    box.getY1() != null && box.getY2() != null) {
                map.put("box", box.getX1() + "," + box.getY1() + "," + box.getX2() + "," + box.getY2());
            }
        }
        if (imageUrl != null) {
            map.put("im_url", imageUrl);
        }
        return map;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
