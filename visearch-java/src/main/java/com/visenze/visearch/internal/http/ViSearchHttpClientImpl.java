package com.visenze.visearch.internal.http;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.visenze.visearch.ViSearchException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViSearchHttpClientImpl implements ViSearchHttpClient {

    private final CloseableHttpClient httpClient;
    private final String accessKey;
    private final String secretKey;

    public ViSearchHttpClientImpl(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        RequestConfig conf = RequestConfig
                .custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(10000)
                .build();
        this.httpClient = HttpClientBuilder
                .create()
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(50)
                .setDefaultRequestConfig(conf)
                .build();
    }

    @Override
    public String get(String url, Multimap<String, String> params) {
        params = addAuthParams(params);
        HttpUriRequest request = buildGetRequest(url, params);
        return executeRequest(request);
    }


    @Override
    public String post(String url, Multimap<String, String> params) {
        params = addAuthParams(params);
        HttpUriRequest request = buildPostRequest(url, params);
        return executeRequest(request);
    }

    @Override
    public String postImage(String url, Multimap<String, String> params, File file) {
        params = addAuthParams(params);
        HttpUriRequest request = buildPostRequest(url, params, file);
        return executeRequest(request);
    }

    @Override
    public String postImage(String url, Multimap<String, String> params, byte[] byteArray, String filename) {
        params = addAuthParams(params);
        HttpUriRequest request = buildPostRequest(url, params, byteArray, filename);
        return executeRequest(request);
    }

    private HttpUriRequest buildGetRequest(String url, Multimap<String, String> params) {
        return RequestBuilder
                .get()
                .setUri(buildGetUri(url, mapToNameValuePair(params)))
                .build();
    }

    private URI buildGetUri(String url, List<NameValuePair> nameValuePairList) {
        try {
            return new URIBuilder(url)
                    .addParameters(nameValuePairList)
                    .build();
        } catch (URISyntaxException e) {
            throw new ViSearchException("Error: URISyntaxException url=" + url + ", params=" + nameValuePairList.toString() + ", error=" + e.getMessage());
        }
    }

    private URI buildPostUri(String url) {
        try {
            return new URIBuilder(url)
                    .build();
        } catch (URISyntaxException e) {
            throw new ViSearchException("Error: URISyntaxException url=" + url + ", error=" + e.getMessage());
        }
    }

    private HttpUriRequest buildPostRequest(String url, Multimap<String, String> params) {
        return RequestBuilder
                .post()
                .setUri(buildPostUri(url))
                .setEntity(new UrlEncodedFormEntity(mapToNameValuePair(params), Consts.UTF_8))
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.withCharset(Consts.UTF_8).toString())
                .build();
    }

    private HttpUriRequest buildMultipartPostRequest(String url, HttpEntity entity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return httpPost;
    }

    private HttpUriRequest buildPostRequest(String url, Multimap<String, String> params, File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("utf-8"));
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN);
        }
        builder.addBinaryBody("image", file);
        HttpEntity entity = builder.build();
        return buildMultipartPostRequest(url, entity);
    }

    private HttpUriRequest buildPostRequest(String url, Multimap<String, String> params, byte[] byteArray, String filename) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN);
        }
        builder.addPart("image", new InputStreamBody(new ByteArrayInputStream(byteArray), filename));
        HttpEntity entity = builder.build();
        return buildMultipartPostRequest(url, entity);
    }

    private String executeRequest(HttpUriRequest request) {
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new ViSearchException("Error: Failed to execute request=" + request.toString() + ", error=" + e.getMessage());
        }
    }

    private Multimap<String, String> addAuthParams(Multimap<String, String> params) {
        Preconditions.checkNotNull(params);
        params.putAll(Multimaps.forMap(ViSearchAuthGenerator.getAuthParams(accessKey, secretKey)));
        return params;
    }

    private List<NameValuePair> mapToNameValuePair(Multimap<String, ?> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, ?> entry : params.entries()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return pairs;
    }

}
