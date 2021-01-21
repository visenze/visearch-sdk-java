package com.visenze.common.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.common.exception.ViException;
import com.visenze.visearch.ResponseMessages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * <h1> 'Box' Container </h1>
 * A 'generic' box class that holds only 4 member variables (corners). It is
 * aimed at supporting creation of a box with integrals or floating points.
 * Since this class acts as a container, it should always be reference frame
 * agnostic. The downside of doing 'generics' is the lack of support for
 * primitive types.
 * <p>
 * However, if the user were to pass in custom data types (i.e. vectors, arrays,
 * matrices), it may result into a cube, trapezium, or even n-th dimensional
 * data type that stems from a box surface.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class ViBox<T> {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * First x-axis
     */
    private T x1;

    /**
     * Second x-axis
     */
    private T x2;

    /**
     * First y-axis
     */
    private T y1;

    /**
     * Second y-axis
     */
    private T y2;

    /**
     * Constructor. The context of the parameters are based off the initial
     * idea that a box class is made of 4 corners of either integrals or
     * floating points.
     *
     * @param x1 first x-axis
     * @param x2 second x-axis
     * @param y1 first y-axis
     * @param y2 second y-axis
     */
    public ViBox(T x1, T x2, T y1, T y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    /**
     * Constructor using an array to allow JSON deserializing. This is needed to
     * work around the fact that the Jackson Annotation is not very friendly
     * with generics. There is no default deserialization as type T.
     *
     * @param dataPoints Array of 4 values
     */
    public ViBox(@NotNull T[] dataPoints) {
        // enforce
        if (dataPoints.length != 4)
            throw new IllegalArgumentException("parameter must be an array of exactly 4!");
        this.x1 = dataPoints[0];
        this.y1 = dataPoints[1];
        this.x2 = dataPoints[2];
        this.y2 = dataPoints[3];
    }

    /**
     * Get the first x-axis value
     *
     * @return T
     */
    public T getX1() {
        return x1;
    }

    /**
     * Set the first x-axis value
     *
     * @param x1 the new value
     */
    public void setX1(T x1) {
        this.x1 = x1;
    }

    /**
     * Get the second x-axis value
     *
     * @return T
     */
    public T getX2() {
        return x2;
    }

    /**
     * Set the second x-axis value
     *
     * @param x2 the new value
     */
    public void setX2(T x2) {
        this.x2 = x2;
    }

    /**
     * Get the first y-axis value
     *
     * @return T
     */
    public T getY1() {
        return y1;
    }

    /**
     * Set the first y-axis value
     *
     * @param y1 the new value
     */
    public void setY1(T y1) {
        this.y1 = y1;
    }

    /**
     * Get the second y-axis value
     *
     * @return T
     */
    public T getY2() {
        return y2;
    }

    /**
     * Set the second y-axis value
     *
     * @param y2 the new value
     */
    public void setY2(T y2) {
        this.y2 = y2;
    }
}
