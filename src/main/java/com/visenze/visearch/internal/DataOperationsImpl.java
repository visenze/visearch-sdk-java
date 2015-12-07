package com.visenze.visearch.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOperationsImpl extends BaseViSearchOperations implements DataOperations {

    public DataOperationsImpl(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper) {
        super(viSearchHttpClient, objectMapper);
    }

    @Override
    public InsertTrans insert(List<Image> imageList) {
        return insert(imageList, new HashMap<String, String>());
    }

    @Override
    public InsertTrans insert(List<Image> imageList, Map<String, String> customParams) {

        Preconditions.checkNotNull(imageList, "image list must not be null");
        Preconditions.checkNotNull(customParams, "custom params must not be null");

        try {
            Multimap<String, String> params = imageListToParams(imageList);
            for (Map.Entry<String, String> entry : customParams.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
            ViSearchHttpResponse httpResponse = viSearchHttpClient.post("/insert", params);
            String response = httpResponse.getBody();
            Map<String, String> headers = httpResponse.getHeaders();
            try {
                JsonNode responseNode = objectMapper.readTree(response);
                JsonNode statusNode = responseNode.get("status");
                if (statusNode == null) {
                    throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, response);
                    // throw new ViSearchException("There was a malformed ViSearch response: " + response, response);
                } else {
                    InsertTrans insertTrans = deserializeObjectResult(response, response, InsertTrans.class);
                    insertTrans.setHeaders(headers);
                    return insertTrans;
                }
            } catch (JsonProcessingException e) {
                throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
                //throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
            } catch (IOException e) {
                throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
                //throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
            }
        } catch (InternalViSearchException e) {
            return new InsertTrans(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    @Override
    public InsertStatus insertStatus(String transId) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(transId), "trans_id must not be null or empty");

        try {
            ViSearchHttpResponse response = viSearchHttpClient.get("/insert/status/" + transId, HashMultimap.<String, String>create());
            return parseInsertStatus(response.getBody(), response.getHeaders());
        } catch (InternalViSearchException e) {
            return new InsertStatus(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    @Override
    public InsertStatus insertStatus(String transId, Integer errorPage, Integer errorLimit) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(transId), "trans_id must not be null or empty");
        Preconditions.checkNotNull(errorPage, "error page must not be null");
        Preconditions.checkNotNull(errorLimit, "error limit must not be null");

        try {
            Multimap<String, String> params = HashMultimap.create();
            params.put("error_page", errorPage.toString());
            params.put("error_limit", errorLimit.toString());
            ViSearchHttpResponse response = viSearchHttpClient.get("/insert/status/" + transId, params);
            return parseInsertStatus(response.getBody(), response.getHeaders());
        } catch (InternalViSearchException e) {
            return new InsertStatus(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    private InsertStatus parseInsertStatus(String response, Map<String, String> headers) {
        try {
            JsonNode responseNode = objectMapper.readTree(response);
            JsonNode statusNode = responseNode.get("status");
            if (statusNode == null) {
                throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, response);
            } else {
                String status = statusNode.asText();
                JsonNode resultArrayNode = responseNode.get("result");
                if ("fail".equals(status) ||
                        resultArrayNode == null || !resultArrayNode.isArray() || resultArrayNode.get(0) == null) {
                    JsonNode errorNode = responseNode.get("error");
                    if (errorNode == null || !errorNode.isArray() || errorNode.get(0) == null) {
                        throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, response);
                    }
                    String message = errorNode.path(0).asText();
                    throw new InternalViSearchException(message, response);
                } else {
                    JsonNode resultNode = resultArrayNode.get(0);
                    InsertStatus insertStatus = deserializeObjectResult(response, resultNode.toString(), InsertStatus.class);
                    insertStatus.setHeaders(headers);
                    return insertStatus;
                }
            }
        } catch (JsonProcessingException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
        }
    }

    @Override
    public RemoveStatus remove(List<String> imNameList) {

        Preconditions.checkNotNull(imNameList, "im_name list for remove must not be null.");

        try {
            Multimap<String, String> params = Multimaps.forMap(imageNameListToParams(imNameList));
            ViSearchHttpResponse httpResponse = viSearchHttpClient.post("/remove", params);
            String response = httpResponse.getBody();
            try {
                JsonNode responseNode = objectMapper.readTree(response);
                JsonNode statusNode = responseNode.get("status");
                JsonNode totalNode = responseNode.get("total");
                if (statusNode == null || totalNode == null) {
                    throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, response);
                } else if ("fail".equals(statusNode.asText())) {
                    JsonNode errorNode = responseNode.get("error");
                    if (errorNode == null || !errorNode.isArray() || errorNode.get(0) == null) {
                        throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, response);
                    }
                    String message = errorNode.path(0).asText();
                    throw new InternalViSearchException(message, response);
                } else {
                    int total = totalNode.asInt();
                    return new RemoveStatus(total);
                }
            } catch (JsonProcessingException e) {
                throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
            } catch (IOException e) {
                throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
            }
        } catch (InternalViSearchException e) {
            return new RemoveStatus(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    private static Multimap<String, String> imageListToParams(List<Image> imageList) {
        Multimap<String, String> params = HashMultimap.create();
        for (int i = 0; i < imageList.size(); i++) {
            Image image = imageList.get(i);
            if (image != null) {
                params.put("im_name" + "[" + i + "]", image.getImName());
                params.put("im_url" + "[" + i + "]", image.getImUrl());
                Map<String, String> metadata = image.getMetadata();
                if (metadata != null) {
                    for (Map.Entry<String, String> entry : metadata.entrySet()) {
                        params.put(entry.getKey() + "[" + i + "]", entry.getValue());
                    }
                }
            }
        }
        return params;
    }

    private static Map<String, String> imageNameListToParams(List<String> imNameList) {
        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < imNameList.size(); i++) {
            String imName = imNameList.get(i);
            if (imName != null) {
                params.put("im_name" + "[" + i + "]", imName);
            }
        }
        return params;
    }

}
