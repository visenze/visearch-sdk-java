package com.visenze.visearch;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * <h1> ProductType </h1>
 * Depending on the image provided, the Visual AI might pick up more than
 * one type of product in the image. This class holds the data of each of these
 * objects detected in the image. It contains information such as "type"
 * (top/eyewear/bottoms/etc), "score" and a bounding "box" of where the object
 * lies on the image space.
 *
 * @since 13 Jan 2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductType {

    private final String type;

    /**
     * The score given to this detected object. If 5 objects are detected and
     * only 3 is requested, only the top 3 scorers are returned. (Confidence?)
     */
    private final Float score;

    /**
     * The bounding box in image space (0,0 top left) that represents where
     * the detected object is at.
     */
    private final List<Integer> box;

    private Map<String, List<String>> attributes;

    private Map<String, List<String>> attributesList;

    public ProductType(String type, Float score, List<Integer> box,
                       Map<String, List<String>> attributes, Map<String, List<String>> attributesList) {
        this.type = type;
        this.score = score;
        this.box = box;
        this.attributes = attributes;
        this.attributesList = attributesList;
    }

    public ProductType(String type, Float score, List<Integer> box) {
        this.type = type;
        this.score = score;
        this.box = box;
    }

    /**
     * Get the object/clothes type of the detection.
     *
     * @return Type of the object detected
     */
    public String getType() {
        return type;
    }

    /**
     * Get the score of the detection.
     *
     * @return Score of the detection
     */
    public Float getScore() {
        return score;
    }

    /**
     * Get the bounding box that identifies the object being detected. The
     * coordinate system is (0,0) top left.
     *
     * @return Bounding box coordinates (image space)
     */
    public List<Integer> getBox() {
        return box;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public Map<String, List<String>> getAttributesList() {
        return attributesList;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "type='" + type + '\'' +
                ", score=" + score +
                ", box=" + box +
                ", attributes=" + attributes +
                ", attributesList=" + attributesList +
                '}';
    }
}
