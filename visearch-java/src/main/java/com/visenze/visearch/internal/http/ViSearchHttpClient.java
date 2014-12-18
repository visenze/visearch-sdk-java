package com.visenze.visearch.internal.http;

import com.google.common.collect.Multimap;

import java.io.File;

public interface ViSearchHttpClient {

    <T> T getForObject(String url, Multimap<String, String> params, Class<T> responseType);

    <T> T postForObject(String url, Multimap<String, String> params, Class<T> responseType);

    <T> T postForObject(String url, Multimap<String, String> params, File imageFile, Class<T> responseType);

    <T> T postForObject(String url, Multimap<String, String> params, byte[] imageByteArray, Class<T> responseType);

    void post(String url, Multimap<String, String> params);

}
