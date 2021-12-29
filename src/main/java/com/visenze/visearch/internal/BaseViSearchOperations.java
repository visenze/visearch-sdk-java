package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

import java.io.IOException;
import java.util.*;

class BaseViSearchOperations {

    final ViSearchHttpClient viSearchHttpClient;
    final ObjectMapper objectMapper;

    BaseViSearchOperations(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper) {
        this.viSearchHttpClient = viSearchHttpClient;
        this.objectMapper = objectMapper;
    }

    protected FeatureResponseResult deserializeFeatureResponseResult(String rawResponse, JsonNode node) {
        JsonNode methodNode = node.get(ViSearchHttpConstants.METHOD);
        if (methodNode == null) {
            throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, rawResponse);
        }

        List<String> result = new ArrayList<String>();

        if(node.has(ViSearchHttpConstants.RESULT)) {
            result = deserializeListResult(rawResponse, node.get(ViSearchHttpConstants.RESULT), String.class);
        }

        FeatureResponseResult featureResult = new FeatureResponseResult(result);

        if(node.has(ViSearchHttpConstants.PRODUCT_TYPES)) {
            List<ProductType> productTypes = deserializeListResult(rawResponse, node.get(ViSearchHttpConstants.PRODUCT_TYPES), ProductType.class);
            featureResult.setProductTypes(productTypes);
        }

        if(node.has(ViSearchHttpConstants.PRODUCT_TYPES_LIST)) {
            List<ProductType> productTypesList = deserializeListResult(rawResponse, node.get(ViSearchHttpConstants.PRODUCT_TYPES_LIST), ProductType.class);
            featureResult.setProductTypesList(productTypesList);
        }

        return featureResult;
    }

    protected PagedSearchResult pagify(String rawResponse, JsonNode node) {

        List<ImageResult> result = new ArrayList<ImageResult>();
        List<ObjectSearchResult> objects = null;
        List<GroupSearchResult> groupResults = null;

        if(node.has(ViSearchHttpConstants.RESULT))
            result = deserializeListResult(rawResponse, node.get(ViSearchHttpConstants.RESULT), ImageResult.class);

        // for merged results, it is possible to have both objects and results
        if (node.has(ViSearchHttpConstants.OBJECTS))
            objects = deserializeListResult(rawResponse, node.get(ViSearchHttpConstants.OBJECTS), ObjectSearchResult.class);

        if (node.has(ViSearchHttpConstants.GROUP_RESULTS))
            groupResults =  deserializeListResult(rawResponse, node.get(ViSearchHttpConstants.GROUP_RESULTS), GroupSearchResult.class);

        JsonNode methodNode = node.get(ViSearchHttpConstants.METHOD);
        if (methodNode == null) {
            throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, rawResponse);
        }
        JsonNode pageNode = node.get(ViSearchHttpConstants.PAGE);
        JsonNode limitNode = node.get(ViSearchHttpConstants.LIMIT);
        JsonNode totalNode = node.get(ViSearchHttpConstants.TOTAL);
        JsonNode groupLimitNode = node.get(ViSearchHttpConstants.GROUP_LIMIT) ;
        JsonNode groupByKeyNode = node.get(ViSearchHttpConstants.GROUP_BY_KEY) ;
        JsonNode algorithmNode = node.get(ViSearchHttpConstants.ALGORITHM);
        JsonNode fallbackAlgorithmNode = node.get(ViSearchHttpConstants.FALLBACK_ALGORITHM);

        PagedSearchResult pagedResult = new PagedSearchResult(result);
        if(pageNode!=null) pagedResult.setPage(pageNode.asInt());
        if(limitNode!=null) pagedResult.setLimit(limitNode.asInt());
        if(totalNode!=null) pagedResult.setTotal(totalNode.asInt());
        if(groupLimitNode!=null) pagedResult.setGroupLimit(groupLimitNode.asInt());
        if(groupByKeyNode!=null) pagedResult.setGroupByKey(groupByKeyNode.asText());
        if(algorithmNode!=null) pagedResult.setAlgorithm(algorithmNode.asText());
        if(fallbackAlgorithmNode!=null) pagedResult.setFallbackAlgorithm(fallbackAlgorithmNode.asText());

        pagedResult.setObjects(objects);
        pagedResult.setGroupSearchResults(groupResults);

        return pagedResult;

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
