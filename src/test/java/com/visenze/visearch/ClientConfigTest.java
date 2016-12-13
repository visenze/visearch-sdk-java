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
        assertTrue(ClientConfig.DEFAULT_USER_AGENT.startsWith("visearch-java-sdk/"+version));
        System.out.println(ClientConfig.DEFAULT_USER_AGENT);
    }

    @Test
    public void getDefaultXRequestWith() throws Exception {
        String version = ViSearch.VISEACH_JAVA_SDK_VERSION;
        assertEquals("visearch-java-sdk/"+version, ClientConfig.DEFAULT_XREQUEST_WITH);
        System.out.println(ClientConfig.DEFAULT_XREQUEST_WITH);
    }

}