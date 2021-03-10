package com.visenze.productsearch.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.visenze.visearch.Box;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;

import java.io.File;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.*;
import static com.visenze.common.util.MultimapUtil.putIfPresent;

/**
 * <h1> SBI Parameters </h1>
 * This class holds the specific parameters required to support Search by Image.
 * It extends the BaseSearchParam to inherit all it's variables that defines all
 * general search parameter.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class SearchByImageParam extends BaseProductSearchParam {

    /**
     * Image URL
     */
    protected String imUrl;

    /**
     * Image ID
     */
    protected String imId;

    /**
     * Image file object (multi-part)
     */
    protected File image;

    /**
     * Optional parameter for restricting the image area x1,y1,x2,y2. The
     * upper-left corner of an image is (0,0).
     */
    protected Optional<Box> box = Optional.absent();

    /**
     * Turn on automatic object detection so the algorithm will try to detect
     * the object in the image and search.
     */
    protected Optional<String> detection = Optional.absent();

    /**
     * Maximum number of products could be detected for a given image; default
     * value is 5. Values must range between 1-30. Returns the objects with
     * higher confidence score first.
     */
    protected Optional<Integer> detectionLimit = Optional.absent();

    /**
     * Parameter to set the detection to more or less sensitive. Default is low.
     */
    protected Optional<String> detectionSensitivity = Optional.absent();

    /**
     * Defaulted to false, if this is set to true, API will return all objects
     */
    protected Optional<Boolean> searchAllObjects = Optional.absent();


    // protected Optional<> point = Optional.absent();


    /**
     * Hide default constructor
     */
    private SearchByImageParam() {
        super();
    }

    /**
     * Method to get constructor using image URL
     *
     * @param imageUrl url to the image
     * @return image param
     */
    public static SearchByImageParam newFromImageUrl(String imageUrl) {
        SearchByImageParam param = new SearchByImageParam();
        if (imageUrl == null || imageUrl.isEmpty())
            throw new InternalViSearchException(ResponseMessages.MISSING_IMAGE_URL);
        param.setImageUrl(imageUrl);
        return param;
    }

    /**
     * Method to get constructor using image ID
     *
     * @param imageId id of the image
     * @return image param
     */
    public static SearchByImageParam newFromImageId(String imageId) {
        SearchByImageParam param = new SearchByImageParam();
        if (imageId == null || imageId.isEmpty())
            throw new InternalViSearchException(ResponseMessages.MISSING_IMAGE_ID);
        param.setImageId(imageId);
        return param;
    }

    /**
     * Method to get constructor using an image file
     *
     * @param imageFile File of the Image
     * @return image param
     */
    public static SearchByImageParam newFromImageFile(File imageFile) {
        SearchByImageParam param = new SearchByImageParam();
        if (imageFile == null)
            throw new InternalViSearchException(ResponseMessages.MISSING_IMAGE_FILE);
        param.setImage(imageFile);
        return param;
    }

    /**
     * Convert this object into it's multimap representation.
     *
     * @return multimap of class variable to value
     */
    @Override
    public Multimap<String, String> toMultimap() {
        Multimap<String, String> multimap = super.toMultimap();
        if (box.isPresent()) {
            multimap.put(BOX, box.get().getX1() + COMMA +
                    box.get().getY1() + COMMA +
                    box.get().getX2() + COMMA +
                    box.get().getY2()
            );
        }

        if (imUrl != null) {
            multimap.put(IM_URL, imUrl);
        }

        if (imId != null) {
            multimap.put(IM_ID, imId);
        }

        setDetectionParams(multimap);

        putIfPresent(multimap, searchAllObjects, SEARCH_ALL_OBJECTS);

        return multimap;
    }

    private void setDetectionParams(Multimap<String, String> multimap) {
        if (detection.isPresent()) {
            multimap.put(DETECTION, detection.get());
        }

        if (detectionLimit.isPresent()) {
            multimap.put(DETECTION_LIMIT, detectionLimit.get().toString());
        }

        if (detectionSensitivity.isPresent()) {
            multimap.put(DETECTION_SENSITIVITY, detectionSensitivity.get());
        }
    }

    /**
     * Get Image url
     *
     * @return imUrl
     */
    @JsonProperty("im_url")
    public String getImageUrl() { return imUrl; }

    /**
     * Set Image url
     *
     * @param imUrl url to image
     */
    public void setImageUrl(String imUrl) { this.imUrl = imUrl; }

    /**
     * Get Image ID
     *
     * @return imId
     */
    @JsonProperty("im_id")
    public String getImageId() {
        return imId;
    }

    /**
     * Set Image ID
     *
     * @param imId image id provided by api response
     */
    public void setImageId(String imId) {
        this.imId = imId;
    }

    /**
     * Get Image file
     *
     * @return image
     */
    @JsonProperty("image")
    public File getImage() {
        return image;
    }

    /**
     * Set Image file
     *
     * @param image image file
     */
    public void setImage(File image) {
        this.image = image;
    }

    /**
     * Get detection box
     *
     * @return box
     */
    public Box getBox() {
        return box.orNull();
    }

    /**
     * Set detection box
     *
     * @param box box coordinates
     */
    public void setBox(Box box) {
        this.box = Optional.fromNullable(box);
    }

    /**
     * Get detecting mode
     *
     * @return detection
     */
    public String getDetection() {
        return detection.orNull();
    }

    /**
     * Set detecting mode
     *
     * @param detection string to use (e.g. 'all', 'eyewear')
     */
    public void setDetection(String detection) {
        this.detection = Optional.fromNullable(detection);
    }

    /**
     * Get the maximum number of products could be detected for
     *
     * @return detectionLimit
     */
    public Integer getDetectionLimit() {
        return detectionLimit.orNull();
    }

    /**
     * Set the maximum number of products that could be detected for.
     * e.g. detectionLimit=3, image can detect up to 3 objects
     *
     * @param detectionLimit maximum detected objects. (1-30)
     */
    public void setDetectionLimit(Integer detectionLimit) {
        this.detectionLimit = Optional.fromNullable(detectionLimit);
    }

    /**
     * Get detection sensitivity, default is low.
     *
     * @return detectionSensitivity
     */
    public String getDetectionSensitivity() {
        return detectionSensitivity.orNull();
    }

    /**
     * Set detection sensitivity, default is low.
     *
     * @param detectionSensitivity sensitivity of detection
     */
    public void setDetectionSensitivity(String detectionSensitivity) {
        this.detectionSensitivity = Optional.fromNullable(detectionSensitivity);
    }

    /**
     * Get searchAllObject
     *
     * @return If searchAllObject is turned on
     */
    public Boolean getSearchAllObjects() { return searchAllObjects.orNull(); }

    /**
     * Set searchAllObject
     *
     * @param b If searchAllObject should be used
     */
    public void setSearchAllObjects(Boolean b) { this.searchAllObjects = Optional.fromNullable(b); }
}
