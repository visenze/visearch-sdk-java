package com.visenze.common.http;

import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import com.visenze.common.exception.ViException;
import com.visenze.visearch.ClientConfig;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;
import org.apache.http.*;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1> HTTP wrapper </h1>
 * This class allows easy use of HTTP methods from java function calls by
 * wrapping certain boiler plate/less important configuration set up. Within
 * this class, to facilitate general usage, 'endpoint urls' refer to the full
 * request destination 'https://www.../.../...'.
 * <p>
 * Overview of what this class does:
 * <br>
 * 1) take in multimap data (most of the time) in it's function parameters
 * <br>
 * 2) format them to work in compliance to what HTTP protocol/library requires
 * <br>
 * 3) performs the HTTP method and returns the response
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class ViHttpClient {

    public static final Charset UTF8_CHARSET = Charset.forName("utf-8") ;

    private final ClientConfig                clientConfig;
    private final CloseableHttpClient         httpClient;
    private final UsernamePasswordCredentials credentials;

    /**
     * Constructor. Creates a HttpClient with the given configurations and
     * credentials. These parameters will become immutable value once inside
     * the class.
     * <p>
     * For both publicKey and privateKey parameter, the value can be anything.
     * Actual username, password, access/secret key, favourite food, car plate,
     * mother's maiden name in reverse alternate case; it does not matter except
     * for the authenticator (who is in backend) that receives this pair of
     * credential needs to know what to do with it.
     *
     * @param config Configurable variables to determine user-agent, time-outs,
     *               sockets, etc...
     * @param access It is forwarded as username for credentials.
     * @param secret It is forwarded as password for credentials.
     *
     * @see ClientConfig
     * @see UsernamePasswordCredentials
     */
    public ViHttpClient(ClientConfig config, String access, String secret) {
        // build the request configuration for HTTP
        final RequestConfig reqConfig = RequestConfig
                .custom()
                .setConnectTimeout(config.getConnectionTimeout())
                .setSocketTimeout(config.getSocketTimeout())
                .build();

        // build the http client based on configurations
        final CloseableHttpClient client = HttpClientBuilder
                .create()
                .setMaxConnTotal(config.getMaxConnection())
                .setMaxConnPerRoute(config.getMaxConnection())
                .setDefaultRequestConfig(reqConfig)
                .build();

        this.clientConfig = config;
        this.httpClient   = client;
        this.credentials  = new UsernamePasswordCredentials(access, secret);
    }

    /**
     * Get the credentials built using publicKey and privateKey.
     *
     * @return UsernamePasswordCredentials
     */
    public UsernamePasswordCredentials getCredentials() {
        return this.credentials;
    }

    /**
     * Performs the GET request to a given endpoint url and a map of request
     * parameters.
     *
     * @param url The endpoint for the request.
     *            e.g. http://visearch.visenze.com/search
     * @param params Multimap of parameter, will be converted into a list of
     *               name-value pairs before sending it in the request
     *               e.g. params["hello"]="world -> .../endpoint?hello=world
     *
     * @return ViHttpResponse of the query
     */
    public ViHttpResponse get(String url, Multimap<String, String> params) {
        // convert the map to a list of parameters and create URI via builder
        URI uri = buildUri(url, convertMapToList(params));
        // create the http request via builder
        HttpUriRequest request = RequestBuilder.get().setUri(uri).build();
        // performs the request and get the response
        return getResponse(request);
    }

    /**
     * Performs the POST request to a given endpoint url and a map of request
     * parameters.
     *
     * @param url The endpoint for the request.
     *            e.g. http://visearch.visenze.com/extractfeature
     * @param params Multimap of parameter, will be converted into a list of
     *               name-value pairs before sending it in the request
     *               e.g. params["hello"]="world -> .../endpoint?hello=world
     *
     * @return ViHttpResponse of the query
     */
    public ViHttpResponse post(String url, Multimap<String, String> params) {
        // build uri without parameters, parameters will be inserted as 'form
        // entities' inside the builder instead
        URI uri = buildUri(url);
        // convert mapped parameters to list
        List<NameValuePair> param_list = convertMapToList(params);
        // build the post request using builder, insert parameters as entity
        HttpUriRequest request = RequestBuilder
                .post()
                .setUri(uri)
                .setEntity(new UrlEncodedFormEntity(param_list, Consts.UTF_8))
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.withCharset(Consts.UTF_8).toString())
                .build();
        // performs the request and get the response
        return getResponse(request);
    }

    /**
     * Performs the POST request with a file that acts as an image 'upload'.
     * Uses MultipartEntityBuilder.
     *
     * @param url The endpoint for the request
     * @param params Multimap of parameter, it will be included into a text body
     *               of the POST request
     * @param file File to 'upload'
     *
     * @return ViHttpResponse of the query
     */
    public ViHttpResponse postImage(String url, Multimap<String, String> params,
                                    File file)
    {
        // create a multipart request that uses a file via builder
        HttpUriRequest request = buildPostRequestForImage(url, params, file);
        // performs the request and get the response
        return getResponse(request);
    }

    /**
     * Performs the POST request with an image stream that acts as a source for
     * image. Uses MultipartEntityBuilder.
     *
     * @param url The endpoint for the request
     * @param params Multimap of parameter, it will be included into a text body
     *               of the POST request
     * @param inputStream
     * @param filename
     *
     * @return ViHttpResponse of the query
     */
    public ViHttpResponse postImage(String url, Multimap<String, String> params,
                                    InputStream inputStream, String filename)
    {
        // create a multipart request that uses an input stream via builder
        HttpUriRequest request = buildPostRequestForImage(url, params, inputStream, filename);
        // performs the request and get the response
        return getResponse(request);
    }

    /**
     * Performs a POST request with image using MultipartEntityBuilder. Using
     * the imFeature parameter directly as a binary body by converting it to
     * it's raw bytes.
     *
     * @param url The endpoint for the request
     * @param params Multimap of parameter, it will be included into a text body
     *               of the POST request
     * @param imFeature
     * @param transId
     *
     * @return ViHttpResponse of the query
     */
    public ViHttpResponse postImFeature(String url,
                                        Multimap<String, String> params,
                                        String imFeature, String transId)
    {
        HttpUriRequest request = buildPostRequestForImFeature(url, params, imFeature);
        // add additional header info if transId is valid
        if (!Strings.isNullOrEmpty(transId)) {
            request.addHeader(ViSearchHttpConstants.TRANS_ID, transId);
        }
        // performs the request and get the response
        return getResponse(request);
    }


    /**
     * Build the URI based on url.
     *
     * @param url The endpoint for the http request
     *
     * @return URI
     */
    private URI buildUri(String url) {
        try {
            return new URIBuilder(url).build();
        } catch (URISyntaxException e) {
            throw new ViException(ResponseMessages.INVALID_ENDPOINT, e);
        }
    }

    /**
     * Build the URI based on url. Overloaded to accept param_list.
     *
     * @param url The endpoint for the http request
     * @param param_list The list of parameters to add into the URI builder
     *
     * @return URI
     */
    private URI buildUri(String url, List<NameValuePair> param_list) {
        try {
            return new URIBuilder(url).addParameters(param_list).build();
        } catch (URISyntaxException e) {
            throw new ViException(ResponseMessages.INVALID_ENDPOINT, e);
        }
    }

    /**
     * Build the POST request using the MultipartEntityBuilder
     *
     * @param url The endpoint for the http POST request
     * @param params Map of data to be included in text body of the POST request
     * @param file The file to 'upload'
     *
     * @return HttpUriRequest
     *
     * @see MultipartEntityBuilder
     */
    private HttpUriRequest buildPostRequestForImage(String url,
                                                    Multimap<String, String> params,
                                                    File file)
    {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        ContentType type = ContentType.TEXT_PLAIN;
        // add parameters as UTF8 encoded plain text to the text body
        builder.setCharset(UTF8_CHARSET);
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), type);
        }
        // add file as binary body into the multipart request
        builder.addBinaryBody(ViSearchHttpConstants.IMAGE, file);
        return buildMultipartPostRequest(url, builder.build());
    }

    /**
     * Build the POST request using the MultipartEntityBuilder
     *
     * @param url The endpoint for the http POST request
     * @param params Map of data to be included in text body of the POST request
     * @param inputStream
     * @param filename
     *
     * @return HttpUriRequest
     *
     * @see MultipartEntityBuilder
     * @see InputStream
     */
    private HttpUriRequest buildPostRequestForImage(String url,
                                                    Multimap<String, String> params,
                                                    InputStream inputStream,
                                                    String filename)
    {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        ContentType type = ContentType.create(
                ContentType.TEXT_PLAIN.getMimeType(), UTF8_CHARSET
        );
        // add parameters to the text body
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), type);
        }
        // add the input stream as part of the request
        InputStreamBody streamBody = new InputStreamBody(inputStream, filename);
        builder.addPart(ViSearchHttpConstants.IMAGE, streamBody);
        return buildMultipartPostRequest(url, builder.build());
    }

    /**
     * Build the POST request using the MultipartEntityBuilder
     *
     * @param url The endpoint for the http POST request
     * @param params Map of data to be included in text body of the POST request
     * @param imFeature
     *
     * @return HttpUriRequest
     *
     * @see MultipartEntityBuilder
     */
    private HttpUriRequest buildPostRequestForImFeature(String url,
                                                        Multimap<String, String> params,
                                                        String imFeature)
    {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        ContentType type = ContentType.create(
                ContentType.TEXT_PLAIN.getMimeType(), UTF8_CHARSET
        );
        // add parameters to the text body
        for (Map.Entry<String, String> entry : params.entries()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), type);
        }
        // add imFeature as binary body by converting string to bytes array
        builder.addBinaryBody(
                ViSearchHttpConstants.IM_FEATURE,
                imFeature.getBytes(),
                type,
                ViSearchHttpConstants.IM_FEATURE
        );
        return buildMultipartPostRequest(url, builder.build());
    }

    /**
     * Build the actual HttpPost request
     *
     * @param url The endpoint for the http POST request
     * @param entity Http Entity built through using the builder found
     *
     * @return HttpUriRequest
     */
    private HttpUriRequest buildMultipartPostRequest(String url,
                                                     HttpEntity entity)
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return httpPost;
    }

    /**
     * Executes the HTTP request. Returns a wrapper to the response, use it to
     * verify if the status of the request.
     *
     * @param request The HttpUriRequest to send.
     *
     * @return ViHttpResponse A wrapper to the actual web response received.
     */
    private ViHttpResponse getResponse(HttpUriRequest request) {
        // update the HTTP request headers
        updateHeaders(request);
        // try execute the HTTP method
        try {
            // the response by HTTP method (actual web response)
            CloseableHttpResponse response = httpClient.execute(request);
            // parse response by us
            return new ViHttpResponse(response);
        } catch(IOException e) {
            throw new ViException(ResponseMessages.NETWORK_ERROR, e);
        } catch (IllegalArgumentException e) {
            throw new ViException(ResponseMessages.SYSTEM_ERROR, e);
        }
    }

    /**
     * Updates the HTTP header fields of the parameter (param object will be
     * modified). For every new general header field to update the request with,
     * do it in this function (after the 'add custom header' comment).
     *
     * @param request The target HttpUriRequest to update
     */
    private void updateHeaders(HttpUriRequest request) {
        try {
            String userAgent = clientConfig.getUserAgent();
            // user agent needs to contain data from the default user agent
            if (!userAgent.equals(ClientConfig.DEFAULT_USER_AGENT)) {
                userAgent += " " + ClientConfig.DEFAULT_USER_AGENT;
            }
            // add authentication header
            request.addHeader(new BasicScheme().authenticate(credentials, request,null));
            // add user agent header
            request.addHeader(HttpHeaders.USER_AGENT, userAgent);
            // add custom header
            request.addHeader(ViSearchHttpConstants.X_REQUESTED_WITH, ClientConfig.DEFAULT_XREQUEST_WITH);
        } catch (AuthenticationException e) {
            throw new ViException(ResponseMessages.UNAUTHORIZED, e);
        }
    }

    /**
     * Converts a Multimap<String, ?> to a List<NameValuePair>. This implies
     * that the secondary type of the multimap must have a ".toString()" that
     * converts the value to its string form.
     *
     * @param params The multimap data of the parameter to send in a HTTP method
     *
     * @return List<NameValuePair> A list of, pairs of strings.
     */
    public static List<NameValuePair> convertMapToList(Multimap<String, ?> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        // go through the map and convert each element to string pairs
        for (Map.Entry<String, ?> entry : params.entries()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return pairs;
    }

}
