package com.visenze.visearch;


public class ResizeSettings {

    public static ResizeSettings STANDARD = new ResizeSettings(512, 512, 85);

    public static ResizeSettings HIGH = new ResizeSettings(1024, 1024, 90);

    private int width;
    private int height;
    private int quality;

    public ResizeSettings(int width, int height, int quality) {
        this.width = width;
        this.height = height;
        if (quality > 100) {
            quality = 100;
        }
        if (quality < 0) {
            quality = 0;
        }
        this.quality = quality;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getQuality() {
        return quality;
    }

}
