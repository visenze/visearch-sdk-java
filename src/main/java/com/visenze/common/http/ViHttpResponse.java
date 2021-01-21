package com.visenze.common.http;

import com.google.common.collect.Maps;
import com.visenze.common.exception.ViException;
import com.visenze.visearch.ResponseMessages;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

/**
 * <h1> HTTP response wrapper </h1>
 * This class acts as a wrapper for an actual HTTP response.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class ViHttpResponse {

    /**
     * Error message, intended to be humanly readable for better debugging.
     */
    private String errorMessage;

    /**
     * The throwable class that caused this response
     */
    private Throwable cause;

    /**
     * The response message unformatted/filtered/altered.
     */
    private String rawResponseMessage;

    /**
     * Map of all headers in the HTTP response.
     */
    protected Map<String, String> headers = Maps.newHashMap();

    /**
     * Set custom header information from a map
     *
     * @param headers HTTP header information
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Set custom header information from an array
     *
     * @param headers HTTP header information
     */
    public void setHeaders(Header[] headers) {
        this.headers.clear();
        if (headers != null)
            for (Header h : headers)
                this.headers.put(h.getName(), h.getValue());
    }

    /**
     * Get header information
     *
     * @return headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Set error message that is supposed to be humanly readable
     *
     * @param errorMessage Message to help with better debugging
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Get the error message
     *
     * @return errorMessage
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Set the cause-er of this response.
     *
     * @param e The throwable object that caused the response
     */
    public void setCause(Throwable e) {
        this.cause = e;
    }

    /**
     * Get the cause-er
     *
     * @return cause
     */
    public Throwable getCause() {
        return this.cause;
    }

    /**
     * Set the unfiltered/altered/unformatted raw message response.
     *
     * @param rawResponseMessage The unfiltered/altered/unformatted raw message
     */
    public void setRawResponseMessage(String rawResponseMessage) {
        this.rawResponseMessage = rawResponseMessage;
    }

    /**
     * Get the raw response message (might be difficult to read)
     *
     * @return rawResponseMessage
     */
    public String getRawResponseMessage() {
        return this.rawResponseMessage;
    }

    /**
     * Body text section of the HTTP response, usually the bulk of the info.
     */
    private String body;

    /**
     * Get the HTTP response body.
     *
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * Constructor using a CloseableHttpResponse
     *
     * @param response A CloseableHttpResponse returned by executing a HTTP
     *                 request.
     */
    public ViHttpResponse(@NotNull CloseableHttpResponse response) {
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new ViException(ResponseMessages.SYSTEM_ERROR, e);
        } catch (IllegalArgumentException e) {
            throw new ViException(ResponseMessages.SYSTEM_ERROR, e);
        }
        setHeaders(response.getAllHeaders());
    }
}
