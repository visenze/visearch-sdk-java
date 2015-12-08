package com.visenze.visearch.internal;

import com.visenze.visearch.ResponseMessages;

public class InternalViSearchException extends RuntimeException {

    private String serverRawResponse;

    public InternalViSearchException(String message, String serverRawResponse) {
        super(message);
        this.serverRawResponse = serverRawResponse;
    }

    public InternalViSearchException(ResponseMessages responseMessages) {
        super(responseMessages.getMessage());
    }

    public InternalViSearchException(ResponseMessages responseMessages, Throwable cause) {
        super(responseMessages.getMessage(), cause);
    }

    public InternalViSearchException(ResponseMessages responseMessages, String serverRawResponse) {
        super(responseMessages.getMessage());
        this.serverRawResponse = serverRawResponse;
    }

    public InternalViSearchException(ResponseMessages responseMessages, Throwable cause, String serverRawResponse) {
        super(responseMessages.getMessage(), cause);
        this.serverRawResponse = serverRawResponse;
    }

    public String getServerRawResponse() {
        return serverRawResponse;
    }
}
