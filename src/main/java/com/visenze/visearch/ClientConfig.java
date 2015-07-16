package com.visenze.visearch;

/**
 * Client config for http connection timeouts, max connections, etc.
 */
public class ClientConfig {

    public static final int DEFAULT_CONNECTION_TIMEOUT = 5 * 1000;

    public static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;

    public static final int DEFAULT_MAX_CONNECTIONS = 50;

    public static final String DEFAULT_USER_AGENT = getDefaultUserAgent();

    private static volatile String defaultUserAgent;

    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    private int maxConnection = DEFAULT_MAX_CONNECTIONS;

    private String userAgent = DEFAULT_USER_AGENT;

    public static String getDefaultUserAgent() {
        if (defaultUserAgent == null) {
            synchronized (ClientConfig.class) {
                if (defaultUserAgent == null) {
                    String platform = "java";
                    String version = ViSearch.VISEACH_JAVA_SDK_VERSION;
                    String osName = System.getProperty("os.name");
                    String osVersion = System.getProperty("os.version");
                    String javaVMName = System.getProperty("java.vm.name");
                    String javaVMVersion = System.getProperty("java.vm.version");
                    String javaVersion = System.getProperty("java.version");
                    String language = System.getProperty("user.language");
                    String region = System.getProperty("user.region");
                    defaultUserAgent = String.format("visearch-sdk-%s/%s ", platform, version);
                    defaultUserAgent += String.format("%s/%s ", osName, osVersion);
                    defaultUserAgent += String.format("%s/%s/%s/%s_%s", javaVMName, javaVMVersion, javaVersion, language, region);
                }
            }
        }
        return defaultUserAgent;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
