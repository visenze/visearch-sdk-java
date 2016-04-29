package com.visenze.visearch;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.InputStream;

public class UploadSearchParams extends BaseSearchParams<UploadSearchParams> {

    private File imageFile;
    private InputStream imageStream;
    private Box box;
    private String imageUrl;
    private String imId;
    private String detection;

    public UploadSearchParams(File imageFile) {
        super();
        Preconditions.checkNotNull(imageFile, "The image file must not be null.");
        this.imageFile = imageFile;
    }

    public UploadSearchParams(InputStream imageStream) {
        super();
        Preconditions.checkNotNull(imageStream, "The image input stream must not be null.");
        this.imageStream = imageStream;
    }

    public UploadSearchParams(String imageUrl) {
        super();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(imageUrl), "The image url must not be null or empty.");
        this.imageUrl = imageUrl;
    }

    public UploadSearchParams() {
        super();
    }

    public UploadSearchParams setBox(Box box) {
        this.box = box;
        return this;
    }

    public Box getBox() {
        return this.box;
    }

    public UploadSearchParams setDetection(String detection) {
        this.detection = detection;
        return this;
    }

    public String getDetection(String detection) {
        return detection;
    }

    public UploadSearchParams setImId(String imId) {
        this.imId = imId;
        return this;
    }

    public String getImId() {
        return imId;
    }

    public File getImageFile() {
        return imageFile;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public String getImageUrl() {
        return imageUrl;
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
        if (imId != null) {
            map.put("im_id", imId);
        }
        if (detection != null) {
            map.put("detection", detection);
        }
        return map;
    }

}
