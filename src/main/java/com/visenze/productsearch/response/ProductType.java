package com.visenze.productsearch.response;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.common.exception.ViException;
import com.visenze.common.util.ViBox;
import com.visenze.visearch.ResponseMessages;

import java.io.IOException;

/**
 * <h1> ProductType </h1>
 * Depending on the image provided, the Visual AI might pick up more than
 * one type of product in the image. This class holds the data of each of these
 * objects detected in the image. It contains information such as "type"
 * (top/eyewear/bottoms/etc), "score" and a bounding "box" of where the object
 * lies on the image space.
 *
 * The @JsonIgnoreProperties(ignoreUnknown=true) annotation tells JSON that this
 * class will ignore deserialization of fields found inside a json object that
 * does not exists within our class (by default error is thrown as we are
 * required to contain every field needed by the json object).
 * <p>
 * This class aims to be Json compatible by implementing Jackson annotation
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 13 Jan 2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductType {

    /**
     * The type of object detected (top/eyewear/bottoms/etc).
     */
    @JsonProperty("type")
    private String type;

    /**
     * The score given to this detected object. If 5 objects are detected and
     * only 3 is requested, only the top 3 scorers are returned. (Confidence?)
     */
    @JsonProperty("score")
    private Float score;

    /**
     * The bounding box in image space (0,0 top left) that represents where
     * the detected object is at.
     *
     * Since this variable is a custom generic by us, we need to tell JSON how
     * to deal with it - via setters and getters
     */
    @JsonProperty("box")
    private ViBox<Integer> box;

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
    public ViBox<Integer> getBox() {
        return box;
    }

    /**
     * Custom setter for JSON to use for deserializing the 'box' field. Because
     * we are using a generic type to hold the 'box' data, we need to have a
     * custom deserializer for it.
     *
     * @param node The extract that represents the 'box' data points (4 corners)
     */
    @JsonSetter("box")
    private void setBox(JsonNode node) {
        final ObjectMapper mapper = new ObjectMapper();
        try{
            // convert the json node into an array of points and use them to
            // construct the generics box class.
            box = new ViBox<Integer>(mapper.treeToValue(node, Integer[].class));
        } catch (IOException e) {
            throw new ViException(ResponseMessages.PARSE_RESPONSE_ERROR,e.getMessage());
        }
    }
}
