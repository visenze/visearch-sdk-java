package com.visenze.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.common.exception.ViException;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * <h1> ViBox Unit Test </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 18 Jan 2021
 */
public class ViBoxTest extends TestCase {
    /**
     * Dummy testing values
     */
    private static final String jsonBox = "[0,100 , 200 , 300]";

    /**
     * Test compatibility with JSON
     */
    @Test
    public void testJackson() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            ViBox<Integer> b1 = new ViBox<Integer>(mapper.treeToValue(mapper.readTree(jsonBox), Integer[].class));
            String temp = jsonBox;
            temp = temp.replace("[","");
            temp = temp.replace("]","");
            temp = temp.replace(" ","");
            List<String> boxVals = new ArrayList<String>(Arrays.asList(temp.split(",")));
            assertEquals(boxVals.get(0), b1.getX1().toString());
            assertEquals(boxVals.get(1), b1.getY1().toString());
            assertEquals(boxVals.get(2), b1.getX2().toString());
            assertEquals(boxVals.get(3), b1.getY2().toString());
        } catch(IllegalArgumentException e) {
            throw new ViException(e.getMessage());
        } catch(IOException e) {
            throw new ViException(e.getMessage());
        }
    }

    /**
     * Test getter of x1
     */
    @Test
    public void testGetX1() {
        Random rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(i,0,0,0);
        ViBox<Float> b2 = new ViBox<Float>(f,0.f,0.f,0.f);

        assertEquals(i, b1.getX1());
        assertEquals(f, b2.getX1());
    }

    /**
     * Test setter of x1
     */
    @Test
    public void testSetX1() {
        Random  rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(0,0,0,0);
        ViBox<Float> b2 = new ViBox<Float>(0.f,0.f,0.f,0.f);

        b1.setX1(i);
        b2.setX1(f);

        assertEquals(i, b1.getX1());
        assertEquals(f, b2.getX1());
    }

    /**
     * Test getter of x2
     */
    @Test
    public void testGetX2() {
        Random  rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(0, i,0,0);
        ViBox<Float> b2 = new ViBox<Float>(0.f, f,0.f,0.f);

        assertEquals(i, b1.getX2());
        assertEquals(f, b2.getX2());
    }

    /**
     * Test setter of x2
     */
    @Test
    public void testSetX2() {
        Random  rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(0,0,0,0);
        ViBox<Float> b2 = new ViBox<Float>(0.f,0.f,0.f,0.f);

        b1.setX2(i);
        b2.setX2(f);

        assertEquals(i, b1.getX2());
        assertEquals(f, b2.getX2());
    }

    /**
     * Test getter of y1
     */
    @Test
    public void testGetY1() {
        Random  rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(0,0, i,0);
        ViBox<Float> b2 = new ViBox<Float>(0.f,0.f, f,0.f);

        assertEquals(i, b1.getY1());
        assertEquals(f, b2.getY1());
    }

    /**
     * Test setter of y1
     */
    @Test
    public void testSetY1() {
        Random  rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(0,0,0,0);
        ViBox<Float> b2 = new ViBox<Float>(0.f,0.f,0.f,0.f);

        b1.setY1(i);
        b2.setY1(f);

        assertEquals(i, b1.getY1());
        assertEquals(f, b2.getY1());
    }

    /**
     * Test getter of y2
     */
    @Test
    public void testGetY2() {
        Random  rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(0,0, 0, i);
        ViBox<Float> b2 = new ViBox<Float>(0.f,0.f,0.f, f);

        assertEquals(i, b1.getY2());
        assertEquals(f, b2.getY2());
    }

    /**
     * Test setter of y2
     */
    @Test
    public void testSetY2() {
        Random  rand = new Random();
        Integer i    = rand.nextInt();
        Float   f    = rand.nextFloat();

        ViBox<Integer> b1 = new ViBox<Integer>(0,0,0,0);
        ViBox<Float> b2 = new ViBox<Float>(0.f,0.f,0.f,0.f);

        b1.setY2(i);
        b2.setY2(f);

        assertEquals(i, b1.getY2());
        assertEquals(f, b2.getY2());
    }
}