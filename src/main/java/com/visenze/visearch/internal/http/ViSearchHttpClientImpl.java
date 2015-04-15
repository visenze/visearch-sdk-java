package com.visenze.visearch.internal.http;

import com.google.common.collect.Multimap;
import com.visenze.visearch.NetworkException;
import com.visenze.visearch.ViSearchException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
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
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViSearchHttpClientImpl implements ViSearchHttpClient {

    private final String endpoint;
    private final CloseableHttpClient httpClient;
    private final UsernamePasswordCredentials credentials;

    public ViSearchHttpClientImpl(String endpoint, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        RequestConfig conf = RequestConfig
                .custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(10000)
                .build();
        credentials = new UsernamePasswordCredentials(accessKey, secretKey);
        this.httpClient = HttpClientBuilder
                .create()
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(50)
                .setDefaultRequestConfig(conf)
                .build();
    }

    @Override
    public String get(String path, Multimap<String, String> params) {
        HttpUriRequest request = buildGetRequest(endpoint + path, params);
        return getStringResponse(request);
    }

    @Override
    public String post(String path, Multimap<String, String> params) {
        HttpUriRequest request = buildPostRequest(endpoint + path, params);
        return getStringResponse(request);
    }

    @Override
    public String postImage(String path, Multimap<String, String> params, File file) {
        HttpUriRequest request = buildPostRequestForImage(endpoint + path, params, file);
        return getStringResponse(request);
    }

    @Override
    public String postImage(String path, Multimap<String, String> params, byte[] byteArray, String filename) {
        HttpUriRequest request = buildPostRequestForImage(endpoint + path, params, byteArray, filename);
        return getStringResponse(request);
    }

    private HttpUriRequest buildGetRequest(String url, Multimap<String, String> params) {
        return RequestBuilder
                .get()
                .setUri(buildGetUri(url, mapToNameValuePair(params)))
                .build();
    }

    private URI buildGetUri(String url, List<NameValuePair> nameValuePairList) {
        try {
            return new URIBuilder(url).addParameters(nameValuePairList).build();
        } catch (URISyntaxException e) {
            throw new ViSearchException("There was an error parsing the ViSearch endpoint. Please ensure " +
                    "that your provided ViSearch endpoint is a well-formed URL and try again.", e);
        }
    }

    private URI buildPostUri(String url) {
        try {
            return new URIBuilder(url).build();
        } catch (URISyntaxException e) {
            throw new ViSearchException("There was an error parsing the ViSearch endpoint. Please ensure " +
                    "that your provided ViSearch endpoint is a well-formed URL and try again.", e);
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

    private HttpUriRequest buildPostRequestForImage(String url, Multimap<String, String> params, File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("utf-8"));
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN);
        }
        builder.addBinaryBody("image", file);
        HttpEntity entity = builder.build();
        return buildMultipartPostRequest(url, entity);
    }

    private HttpUriRequest buildPostRequestForImage(String url, Multimap<String, String> params, byte[] byteArray, String filename) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN);
        }
        builder.addPart("image", new InputStreamBody(new ByteArrayInputStream(byteArray), filename));
        HttpEntity entity = builder.build();
        return buildMultipartPostRequest(url, entity);
    }

    private String getStringResponse(HttpUriRequest request) {
        addAuthHeader(request);
        CloseableHttpResponse response = executeRequest(request);
        return getStringFromEntity(response);
    }

    private void addAuthHeader(HttpUriRequest request) {
        try {
            request.addHeader(new BasicScheme().authenticate(credentials, request, null));
        } catch (AuthenticationException e) {
            throw new com.visenze.visearch.AuthenticationException("There was an error generating the " +
                    "HTTP basic authentication header. Please check your access key and secret key and try again", e);
        }
    }

    private CloseableHttpResponse executeRequest(HttpUriRequest request) {
        try {
            return httpClient.execute(request);
        } catch (IOException e) {
            throw new NetworkException("A network error occurred when requesting to the ViSearch endpoint. " +
                    "Please check your network connectivity and try again.", e);
        }
    }

    private String getStringFromEntity(CloseableHttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            throw new NetworkException("A network error occurred when reading response from the ViSearch endpoint. " +
                    "Please check your network connectivity and try again.", e);
        } catch (IllegalArgumentException e) {
            throw new NetworkException("A network error occurred when reading response from the ViSearch endpoint. " +
                    "Please check your network connectivity and try again.", e);
        }
    }

    private List<NameValuePair> mapToNameValuePair(Multimap<String, ?> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, ?> entry : params.entries()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return pairs;
    }

}
