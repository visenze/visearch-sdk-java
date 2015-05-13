package com.visenze.visearch;


public class AuthenticationException extends ViSearchException {

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message, String json) {
        super(message, json);
    }

}
