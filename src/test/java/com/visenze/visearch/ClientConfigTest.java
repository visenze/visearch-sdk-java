package com.visenze.visearch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dejun on 13/12/16.
 */
public class ClientConfigTest {
    @Test
    public void getDefaultUserAgent() throws Exception {
        String version = ViSearch.VISEACH_JAVA_SDK_VERSION;
        assertTrue(ClientConfig.DEFAULT_USER_AGENT.startsWith("visearch-sdk-java/"+version));
    }

    @Test
    public void getDefaultXRequestWith() throws Exception {
        String version = ViSearch.VISEACH_JAVA_SDK_VERSION;
        assertEquals("visearch-sdk-java/"+version, ClientConfig.DEFAULT_XREQUEST_WITH);
    }

}