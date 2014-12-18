package com.visenze.visearch.internal.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.visenze.visearch.ViSearchException;
import com.visenze.visearch.internal.util.AuthGenerator;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViSearchHttpClientImpl implements ViSearchHttpClient {

    private final CloseableHttpClient httpClient;
    private final String accessKey;
    private final String secretKey;
    private final ObjectMapper mapper;

    public ViSearchHttpClientImpl(String accessKey, String secretKey, ObjectMapper mapper) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.mapper = mapper;
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
    public <T> T getForObject(String url, Multimap<String, String> params, Class<T> clazz) throws ViSearchException {
        params = addAuthParams(params);
        HttpUriRequest request = buildGetRequest(url, params);
        String response = executeRequest(request);
        return parseJsonForObject(response, clazz);
    }


    @Override
    public <T> T postForObject(String url, Multimap<String, String> params, Class<T> clazz) throws ViSearchException {
        params = addAuthParams(params);
        HttpUriRequest request = buildPostRequest(url, params);
        String response = executeRequest(request);
        return parseJsonForObject(response, clazz);
    }

    @Override
    public <T> T postForObject(String url, Multimap<String, String> params, File imageFile, Class<T> clazz) {
        params = addAuthParams(params);
        HttpUriRequest request = buildPostRequest(url, params, imageFile);
        String response = executeRequest(request);
        return parseJsonForObject(response, clazz);
    }

    @Override
    public <T> T postForObject(String url, Multimap<String, String> params, byte[] imageByteArray, Class<T> clazz) {
        params = addAuthParams(params);
        HttpUriRequest request = buildPostRequest(url, params, imageByteArray);
        String response = executeRequest(request);
        return parseJsonForObject(response, clazz);
    }

    @Override
    public void post(String url, Multimap<String, String> params) {
        params = addAuthParams(params);
        HttpUriRequest request = buildPostRequest(url, params);
        executeRequest(request);
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
            throw new ViSearchException("Error: UIRSyntaxException url=" + url + ", params=" + nameValuePairList.toString() + ", error=" + e.getMessage());
        }
    }

    private <T> T parseJsonForObject(String json, Class<T> clazz) {
        try {
            return mapper.reader(clazz).readValue(json);
        } catch (Exception e) {
            throw new ViSearchException("Error: Failed to process json=" + json + ", error=" + e.getMessage());
        }
    }

    private URI buildPostUri(String url) {
        try {
            return new URIBuilder(url)
                    .build();
        } catch (URISyntaxException e) {
            throw new ViSearchException("Error: UIRSyntaxException url=" + url + ", error=" + e.getMessage());
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
        return RequestBuilder
                .post()
                .setUri(buildPostUri(url))
                .setEntity(entity)
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.MULTIPART_FORM_DATA.withCharset(Consts.UTF_8).toString())
                .build();
    }

    private HttpUriRequest buildPostRequest(String url, Multimap<String, String> params, File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.APPLICATION_FORM_URLENCODED.withCharset(Consts.UTF_8));
        }
        FileBody fileBody = new FileBody(file);
        builder.addPart("image", fileBody);
        HttpEntity entity = builder.build();
        return buildMultipartPostRequest(url, entity);
    }

    private HttpUriRequest buildPostRequest(String url, Multimap<String, String> params, byte[] byteArray) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.APPLICATION_FORM_URLENCODED.withCharset(Consts.UTF_8));
        }
        ByteArrayBody byteArrayBody = new ByteArrayBody(byteArray, ContentType.MULTIPART_FORM_DATA, "image");
        builder.addPart("image", byteArrayBody);
        HttpEntity entity = builder.build();
        return buildMultipartPostRequest(url, entity);
    }

    private String executeRequest(HttpUriRequest request) {
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new ViSearchException("Error: Failed to execute request request=" + request.toString() + ", error=" + e.getMessage());
        }
    }

    private Multimap<String, String> addAuthParams(Multimap<String, String> params) {
        Preconditions.checkNotNull(params);
        params.putAll(Multimaps.forMap(AuthGenerator.getAuthParams(accessKey, secretKey)));
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
