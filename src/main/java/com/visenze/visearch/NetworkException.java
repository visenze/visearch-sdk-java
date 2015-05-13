package com.visenze.visearch;


public class NetworkException extends ViSearchException {

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(String message) {
        super(message);
    }
}
