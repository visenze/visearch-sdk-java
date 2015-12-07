package com.visenze.visearch.internal.http;

import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;
import com.visenze.visearch.internal.ResponseBase;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ViSearchHttpResponse extends ResponseBase {
    private String body;
    public ViSearchHttpResponse(CloseableHttpResponse response) {
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.SYSTEM_ERROR, e);
            // throw new NetworkException("A network error occurred when reading response from the ViSearch endpoint. " +
            //         "Please check your network connectivity and try again.", e);
        } catch (IllegalArgumentException e) {
            throw new InternalViSearchException(ResponseMessages.SYSTEM_ERROR, e);
            // throw new NetworkException("A network error occurred when reading response from the ViSearch endpoint. " +
            //        "Please check your network connectivity and try again.", e);
        }
    }
    public String getBody() {
        return body;
    }
}
