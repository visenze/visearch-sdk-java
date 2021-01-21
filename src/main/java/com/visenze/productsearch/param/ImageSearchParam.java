package com.visenze.productsearch.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.visenze.visearch.Box;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;

import java.io.File;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.*;
import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.DETECTION_SENSITIVITY;

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
public class ImageSearchParam extends BaseProductSearchParam {

    /**
     * Image URL
     */
    protected String im_url;

    /**
     * Image ID
     */
    protected String im_id;

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
    protected Optional<Integer> detection_limit = Optional.absent();

    /**
     * Parameter to set the detection to more or less sensitive. Default is low.
     */
    protected Optional<String> detection_sensitivity = Optional.absent();

    /**
     * Constructor that must have one of the string parameters assigned with
     * valid values.
     *
     *      Both - GOOD but unsure which will be used,
     *      Only One - GOOD,
     *      Neither - BAD
     *
     * @param image_id image id
     * @param image_url image url
     */
    public ImageSearchParam(String image_url, String image_id) {
        super();
        boolean invalidImageId = image_id == null || image_id.isEmpty();
        boolean invalidImageUrl = image_url == null || image_url.isEmpty();
        if (invalidImageId && invalidImageUrl)
            throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_OR_URL);
        this.im_url = image_url;
        this.im_id = image_id;
        this.image = null;
    }

    /**
     * Constructor using an image file
     *
     * @param image_file File of the Image
     */
    public ImageSearchParam(File image_file) {
        super();
        Preconditions.checkNotNull(image_file, "The image file must not be null.");
        this.image = image_file;
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
        if (im_url != null) {
            multimap.put(IM_URL, im_url);
        }
        if (im_id != null) {
            multimap.put(IM_ID, im_id);
        }
        if (detection.isPresent()) {
            multimap.put(DETECTION, detection.get());
        }
        if (detection_limit.isPresent()) {
            multimap.put(DETECTION_LIMIT, detection_limit.get().toString());
        }
        if (detection_sensitivity.isPresent()) {
            multimap.put(DETECTION_SENSITIVITY, detection_sensitivity.get());
        }
        return multimap;
    }

    /**
     * Get Image url
     *
     * @return im_url
     */
    @JsonProperty("im_url")
    public String getImageUrl() { return im_url; }

    /**
     * Set Image url
     *
     * @param im_url url to image
     */
    public void setImageUrl(String im_url) { this.im_url = im_url; }

    /**
     * Get Image ID
     *
     * @return im_id
     */
    @JsonProperty("im_id")
    public String getImageId() {
        return im_id;
    }

    /**
     * Set Image ID
     *
     * @param im_id image id provided by api response
     */
    public void setImageId(String im_id) {
        this.im_id = im_id;
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
     * @return detection_limit
     */
    public Integer getDetectionLimit() {
        return detection_limit.orNull();
    }

    /**
     * Set the maximum number of products that could be detected for.
     * e.g. detection_limit=3, image can detect up to 3 objects
     *
     * @param detection_limit maximum detected objects. (1-30)
     */
    public void setDetectionLimit(Integer detection_limit) {
        this.detection_limit = Optional.fromNullable(detection_limit);
    }

    /** Get detection sensitivity, default is low.
     *
     * @return detection_sensitivity
     */
    public String getDetectionSensitivity() {
        return detection_sensitivity.orNull();
    }

    /** Sett detection sensitivity, default is low.
     *
     * @param detection_sensitivity sensitivity of detection
     */
    public void setDetectionSensitivity(String detection_sensitivity) {
        this.detection_sensitivity = Optional.fromNullable(detection_sensitivity);
    }

}
