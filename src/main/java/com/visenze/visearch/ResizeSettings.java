package com.visenze.visearch;


/**
 * ResizeSettings is deprecated and has no effect on the upload search image.
 * @deprecated
 */
@Deprecated
public class ResizeSettings {

    public static final ResizeSettings STANDARD = new ResizeSettings(512, 512, 0.97f);

    public static final ResizeSettings HIGH = new ResizeSettings(1024, 1024, 0.985f);

    private static final float DELTA = 0.000001f;

    private int width;
    private int height;
    private float quality;

    public ResizeSettings(int width, int height, float quality) {
        this.width = width;
        this.height = height;
        if (quality - DELTA < 0) {
            this.quality = 0;
        } else if (quality + DELTA > 1.0f) {
            this.quality = 1.0f;
        } else {
            this.quality = quality;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getQuality() {
        return quality;
    }

}
