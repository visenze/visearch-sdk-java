package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.visenze.visearch.PagedResult;
import com.visenze.visearch.ViSearchException;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.json.ViSenzeModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AbstractViSenzeOperations {

    protected ViSearchHttpClient viSearchHttpClient;
    protected String endpoint;

    public AbstractViSenzeOperations(ViSearchHttpClient viSearchHttpClient, String endpoint) {
        this.viSearchHttpClient = viSearchHttpClient;
        this.endpoint = endpoint;
    }

    protected <T> PagedResult<T> pagify(JsonNode node, Class<T> clazz) {
        List<T> result = deserializeListResult(node.get("result"), clazz);
        Integer page = node.get("page").asInt();
        Integer limit = node.get("limit").asInt();
        Integer total = node.get("total").asInt();
        return new PagedResult<T>(page, limit, total, result);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> deserializeListResult(JsonNode node, Class<T> clazz) {
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
            return (List<T>) new ObjectMapper().registerModule(new ViSenzeModule()).reader(listType).readValue(node.toString());
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing list result");
        }
    }

    @SuppressWarnings("unchecked")
    protected <T, U> Map<T, U> deserializeMapResult(JsonNode node, Class<T> keyClass, Class<T> valueClass) {
        try {
            MapType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, keyClass, valueClass);
            return (Map<T, U>) new ObjectMapper().registerModule(new ViSenzeModule()).reader(mapType).readValue(node);
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing map result");
        }
    }
}
