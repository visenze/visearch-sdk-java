package com.visenze.visearch.internal.http;

import com.google.common.collect.Multimap;

import java.io.File;
import java.io.InputStream;

public interface ViSearchHttpClient {

    ViSearchHttpResponse get(String url, Multimap<String, String> params);

    ViSearchHttpResponse post(String url, Multimap<String, String> params);

    ViSearchHttpResponse postImage(String url, Multimap<String, String> params, File file);

    ViSearchHttpResponse postImage(String url, Multimap<String, String> params, InputStream inputStream, String filename);

}
