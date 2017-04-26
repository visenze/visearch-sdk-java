package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.visenze.visearch.ImageResult;
import com.visenze.visearch.PagedResult;
import com.visenze.visearch.PagedSearchResult;
import com.visenze.visearch.ResponseMessages;
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

    <T> PagedResult<T> pagify(String rawResponse, String json, Class<T> clazz) {
        try {
            JsonNode node = objectMapper.readTree(json);
            List<T> result = new ArrayList<T>();
            if(node.has("result"))
                result = deserializeListResult(rawResponse, node.get("result"), clazz);
            else if (node.has("group_result"))
                result = (List<T>) deserializeListMultiResult(rawResponse, node.get("group_result"), ImageResult.class);
            JsonNode pageNode = node.get("page");
            JsonNode limitNode = node.get("limit");
            JsonNode totalNode = node.get("total");
            if (pageNode == null || limitNode == null || totalNode == null) {
                throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, rawResponse);
                // throw new ViSearchException("Could not parse the paged ViSearch response: " + json, json);
            }
            return new PagedResult<T>(pageNode.asInt(), limitNode.asInt(), totalNode.asInt(), result);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
            // throw new ViSearchException("Could not parse the paged ViSearch response: " + json, e, json);
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

    <T> List<T> deserializeListMultiResult(String rawResponse, JsonNode node, Class<T> clazz) {
        try {
            List<T> list = new ArrayList<T>();
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, ImageResult.class);
            for(int i=0; i<node.size(); i++) {
                String json = node.get(i).toString();
                list.add((T) objectMapper.readerFor(listType).readValue(json));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
//             throw new ViSearchException("Could not parse the ViSearch response for list of " +
//                    clazz.getSimpleName() + ": " + json, e, json);
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
//             throw new ViSearchException("Could not parse the ViSearch response for list of " +
//                    clazz.getSimpleName() + ": " + json, e, json);
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
            //throw new ViSearchException("Could not parse the ViSearch response for map<" +
            //        keyClass.getSimpleName() + ", " +
            //        valueClass.getSimpleName() + ">: " + json, e, json);
        }
    }
}
