package com.visenze.visearch.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.visenze.visearch.PagedResult;
import com.visenze.visearch.ViSearchException;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

import java.io.IOException;
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

    <T> PagedResult<T> pagify(String json, Class<T> clazz) {
        try {
            JsonNode node = objectMapper.readTree(json);
            List<T> result = deserializeListResult(node.get("result"), clazz);
            JsonNode pageNode = node.get("page");
            JsonNode limitNode = node.get("limit");
            JsonNode totalNode = node.get("total");
            if (pageNode == null || limitNode == null || totalNode == null) {
                throw new ViSearchException("Could not parse the paged ViSearch response: " + json, json);
            }
            return new PagedResult<T>(pageNode.asInt(), limitNode.asInt(), totalNode.asInt(), result);
        } catch (JsonProcessingException e) {
            throw new ViSearchException("Could not parse the paged ViSearch response: " + json, e, json);
        } catch (IOException e) {
            throw new ViSearchException("Could not parse the paged ViSearch response: " + json, e, json);
        }
    }

    <T> T deserializeObjectResult(String json, Class<T> clazz) {
        try {
            return objectMapper.reader(clazz).readValue(json);
        } catch (JsonProcessingException e) {
            throw new ViSearchException("Could not parse the ViSearch response for " +
                    clazz.getSimpleName() + ": " + json, e, json);
        } catch (IOException e) {
            throw new ViSearchException("Could not parse the ViSearch response for " +
                    clazz.getSimpleName() + ": " + json, e, json);
        }
    }

    @SuppressWarnings("unchecked")
    <T> List<T> deserializeListResult(JsonNode node, Class<T> clazz) {
        String json = node.toString();
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
            return (List<T>) objectMapper.reader(listType).readValue(json);
        } catch (JsonProcessingException e) {
            throw new ViSearchException("Could not parse the list ViSearch response for " +
                    clazz.getSimpleName() + ": " + json, e, json);
        } catch (IOException e) {
            throw new ViSearchException("Could not parse the list ViSearch response for " +
                    clazz.getSimpleName() + ": " + json, e, json);
        }
    }

    @SuppressWarnings("unchecked")
    <T, U> Map<T, U> deserializeMapResult(JsonNode node, Class<T> keyClass, Class<T> valueClass) {
        String json = node.toString();
        try {
            MapType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, keyClass, valueClass);
            return (Map<T, U>) objectMapper.reader(mapType).readValue(node);
        } catch (JsonProcessingException e) {
            throw new ViSearchException("Could not parse the map ViSearch response for " +
                    keyClass.getSimpleName() + ", " +
                    valueClass.getSimpleName() + ": " + json, e, json);
        } catch (IOException e) {
            throw new ViSearchException("Could not parse the map ViSearch response for " +
                    keyClass.getSimpleName() + ", " +
                    valueClass.getSimpleName() + ": " + json, e, json);
        }
    }
}
