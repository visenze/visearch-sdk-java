package com.visenze.visearch;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.InputStream;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.*;

public class UploadSearchParams extends BaseSearchParams<UploadSearchParams> {


    private File imageFile;
    private InputStream imageStream;
    private Box box;
    private String imageUrl;
    private String imId;
    private String detection;
    private String imFeature;

    // required for search with imFeature for image to appear in Upload History
    private String transId;

    // discover search
    private Integer detectionLimit ;
    private Integer resultLimit ;
    private String detectionSensitivity ;

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

    public UploadSearchParams setImId(String imId) {
        this.imId = imId;
        return this;
    }

    public UploadSearchParams setImFeature(String imFeature){
        this.imFeature = imFeature;
        return this;
    }

    public UploadSearchParams setTransId(String transId) {
        this.transId = transId;
        return this;
    }

    public UploadSearchParams setDetectionLimit(Integer detectionLimit) {
        this.detectionLimit = detectionLimit;
        return this;
    }

    public UploadSearchParams setResultLimit(Integer resultLimit) {
        this.resultLimit = resultLimit;
        return this;
    }

    public UploadSearchParams setDetectionSensitivity(String detectionSensitivity) {
        this.detectionSensitivity = detectionSensitivity;
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

    public String getImFeature() {
        return imFeature;
    }

    public String getTransId() {
        return transId;
    }

    public String getDetection() {
        return detection;
    }

    public Integer getDetectionLimit() {
        return detectionLimit;
    }

    public Integer getResultLimit() {
        return resultLimit;
    }

    public String getDetectionSensitivity() {
        return detectionSensitivity;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (box != null && box.allCoordsExist()) {
            map.put(BOX, box.getX1() + COMMA + box.getY1() + COMMA + box.getX2() + COMMA + box.getY2());
        }
        if (imageUrl != null) {
            map.put(IM_URL, imageUrl);
        }
        if (imId != null) {
            map.put(IM_ID, imId);
        }
        if (detection != null) {
            map.put(DETECTION, detection);
        }
        if (detectionLimit != null) {
            map.put(DETECTION_LIMIT, detectionLimit.toString());
        }
        if (resultLimit != null) {
            map.put(RESULT_LIMIT, resultLimit.toString());
        }
        if (detectionSensitivity != null) {
            map.put(DETECTION_SENSITIVITY, detectionSensitivity.toString());
        }

        return map;
    }

}
