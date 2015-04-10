package com.visenze.visearch;

public class ViSearchException extends RuntimeException {

    private String json;

    public ViSearchException(String message) {
        super(message);
    }

    public ViSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViSearchException(String message, String json) {
        super(message);
        this.json = json;
    }

    public String getJson() {
        return json;
    }
}
