package com.visenze.visearch.internal;

import com.visenze.visearch.ResponseMessages;

/**
 * <h1> ViSenze exception object </h1>
 * This class allows custom exceptions for internal usage such as storing
 * replies from our servers, logging of internal response messages.
 *
 * @since 08 Jan 2021
 */
public class InternalViSearchException extends RuntimeException {

    /**
     * Raw reply from server
     */
    private String serverRawResponse;

    /**
     * Constructor
     *
     * @param message Message for runtime exception
     * @param serverRawResponse Server response for internal usage
     */
    public InternalViSearchException(String message, String serverRawResponse) {
        super(message);
        this.serverRawResponse = serverRawResponse;
    }

    /**
     * Constructor
     *
     * @param message Message for runtime exception
     */
    public InternalViSearchException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param responseMessages Internal response message to convert for runtime
     *                         exception
     * @see ResponseMessages
     */
    public InternalViSearchException(ResponseMessages responseMessages) {
        super(responseMessages.getMessage());
    }

    /**
     * Constructor
     *
     * @param responseMessages Internal response message to convert for runtime
     *                         exception
     * @param cause Throwable object
     *
     * @see ResponseMessages
     */
    public InternalViSearchException(ResponseMessages responseMessages,
                                     Throwable cause)
    {
        super(responseMessages.getMessage(), cause);
    }

    /**
     * Constructor
     *
     * @param responseMessages Internal response message to convert for runtime
     *                         exception
     * @param serverRawResponse Server response for internal usage
     *
     * @see ResponseMessages
     */
    public InternalViSearchException(ResponseMessages responseMessages,
                                     String serverRawResponse)
    {
        super(responseMessages.getMessage());
        this.serverRawResponse = serverRawResponse;
    }

    /**
     * Constructor
     *
     * @param responseMessages Internal response message to convert for runtime
     *                         exception
     * @param cause Throwable object
     * @param serverRawResponse Server response for internal usage
     *
     * @see ResponseMessages
     */
    public InternalViSearchException(ResponseMessages responseMessages,
                                     Throwable cause,
                                     String serverRawResponse)
    {
        super(responseMessages.getMessage(), cause);
        this.serverRawResponse = serverRawResponse;
    }

    /**
     * Get the server's raw response
     *
     * @return raw message from server
     */
    public String getServerRawResponse() {
        return serverRawResponse;
    }
}
