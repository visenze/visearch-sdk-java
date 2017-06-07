package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BaseViSearchOperations {

    final ViSearchHttpClient viSearchHttpClient;
    final ObjectMapper objectMapper;

    BaseViSearchOperations(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper) {
        this.viSearchHttpClient = viSearchHttpClient;
        this.objectMapper = objectMapper;
    }

    PagedSearchResult pagify(String rawResponse, String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            List<ImageResult> result = new ArrayList<ImageResult>();
            List<ObjectSearchResult> objects = null;
            if(node.has("result"))
                result = deserializeListResult(rawResponse, node.get("result"), ImageResult.class);
            else if (node.has("objects"))
                objects = deserializeListResult(rawResponse, node.get("objects"), ObjectSearchResult.class);
            JsonNode methodNode = node.get("method");
            if (methodNode == null) {
                throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, rawResponse);
            }
            JsonNode pageNode = node.get("page");
            JsonNode limitNode = node.get("limit");
            JsonNode totalNode = node.get("total");
            PagedSearchResult pagedResult = new PagedSearchResult(result);
            if(pageNode!=null) pagedResult.setPage(pageNode.asInt());
            if(limitNode!=null) pagedResult.setLimit(limitNode.asInt());
            if(totalNode!=null) pagedResult.setTotal(totalNode.asInt());
            pagedResult.setObjects(objects);
            return pagedResult;
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
        }
    }

    <T> T deserializeObjectResult(String rawResponse, String json, Class<T> clazz) {
        try {
            return objectMapper.reader(clazz).readValue(json);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
            // throw new ViSearchException("Could not parse the ViSearch response for " +
            //        clazz.getSimpleName() + ": " + json, e, json);
        }
    }

    @SuppressWarnings("unchecked")
    <T> List<T> deserializeListResult(String rawResponse, JsonNode node, Class<T> clazz) {
        String json = node.toString();
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
            return (List<T>) objectMapper.readerFor(listType).readValue(json);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
        }
    }

    @SuppressWarnings("unchecked")
    <T, U> Map<T, U> deserializeMapResult(String rawResponse, JsonNode node, Class<T> keyClass, Class<T> valueClass) {
        String json = node.toString();
        try {
            MapType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, keyClass, valueClass);
            return (Map<T, U>) objectMapper.readerFor(mapType).readValue(node);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
        }
    }
}
