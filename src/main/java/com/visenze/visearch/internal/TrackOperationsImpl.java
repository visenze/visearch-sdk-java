package com.visenze.visearch.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.util.Map;

/**
 * Created by dejun on 15/9/16.
 */
public class TrackOperationsImpl implements TrackOperations {

    final ViSearchHttpClient viSearchHttpClient;

    private static final String ENDPOINT_SEND_ACTIONS = "/__aq.gif";
    private final String userId;

    public TrackOperationsImpl(ViSearchHttpClient viSearchHttpClient) {
        this.viSearchHttpClient = viSearchHttpClient;
        UsernamePasswordCredentials credentials = this.viSearchHttpClient.getCredentials();
        if(credentials!=null) {
            userId = credentials.getUserName();
        }else {
            userId = "";
        }
    }

    /**
     * send event
     * @param params
     */
    public void sendEvent(Map<String, String> params) {
        // put user id
        params.put("cid", userId);
        Multimap queryParams = HashMultimap.create();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Preconditions.checkNotNull(key, "Custom search param key must not be null.");
            Preconditions.checkNotNull(value, "Custom search param value must not be null.");
            queryParams.put(key, value);
        }
        viSearchHttpClient.get(ENDPOINT_SEND_ACTIONS, queryParams);
    }
}
