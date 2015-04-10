package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.visenze.visearch.PagedResult;
import com.visenze.visearch.ViSearchException;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

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
            Integer page = node.get("page").asInt();
            Integer limit = node.get("limit").asInt();
            Integer total = node.get("total").asInt();
            return new PagedResult<T>(page, limit, total, result);
        } catch (Exception e) {
            throw new ViSearchException("Error pagifying json=" + json);
        }
    }

    <T> T deserializeObjectResult(String json, Class<T> clazz) {
        try {
            return objectMapper.reader(clazz).readValue(json);
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing object result");
        }
    }

    @SuppressWarnings("unchecked")
    <T> List<T> deserializeListResult(JsonNode node, Class<T> clazz) {
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
            return (List<T>) objectMapper.reader(listType).readValue(node.toString());
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing list result");
        }
    }

    @SuppressWarnings("unchecked")
    <T, U> Map<T, U> deserializeMapResult(JsonNode node, Class<T> keyClass, Class<T> valueClass) {
        try {
            MapType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, keyClass, valueClass);
            return (Map<T, U>) objectMapper.reader(mapType).readValue(node);
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing map result");
        }
    }
}
