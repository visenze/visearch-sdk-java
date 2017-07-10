package com.visenze.visearch.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchOperationsImpl extends BaseViSearchOperations implements SearchOperations {

    private static final String ENDPOINT_DISCOVER_SEARCH = "/discoversearch";
    private static final String ENDPOINT_UPLOAD_SEARCH = "/uploadsearch";
    private static final String ENDPOINT_SEARCH = "/search";
    private static final String ENDPOINT_RECOMMENDATION = "/recommendation";
    private static final String ENDPOINT_COLOR_SEARCH = "/colorsearch";
    private static final String ENDPOINT_SIMILAR_PRODUCTS_SEARCH = "/similarproducts";
    private static final String ENDPOINT_EXTRACT_FEATURE= "/extractfeature";

    public SearchOperationsImpl(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper) {
        super(viSearchHttpClient, objectMapper);
    }

    @Override
    public PagedSearchResult search(SearchParams searchParams) {
        try {
            ViSearchHttpResponse response = viSearchHttpClient.get(ENDPOINT_SEARCH, searchParams.toMap());
            return getPagedResult(response);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    @Override
    public PagedSearchResult recommendation(SearchParams searchParams) {
        try {
            ViSearchHttpResponse response = viSearchHttpClient.get(ENDPOINT_RECOMMENDATION, searchParams.toMap());
            return getPagedResult(response);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    @Override
    public PagedSearchResult colorSearch(ColorSearchParams colorSearchParams) {
        try {
            ViSearchHttpResponse response = viSearchHttpClient.get(ENDPOINT_COLOR_SEARCH, colorSearchParams.toMap());
            return getPagedResult(response);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams) {
        try {
            return postImageSearch(uploadSearchParams, ENDPOINT_UPLOAD_SEARCH);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }


    /**
     * Perform real disover search
     * @param uploadSearchParams
     * @return
     */
    @Override
    public PagedSearchResult discoverSearch(UploadSearchParams uploadSearchParams) {
        try {
            return postImageSearch(uploadSearchParams, ENDPOINT_DISCOVER_SEARCH);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    /**
     * Perform real disover search
     * @param uploadSearchParams
     * @return
     */
    @Override
    public PagedSearchResult similarProductsSearch(UploadSearchParams uploadSearchParams) {
        try {
            return postImageSearch(uploadSearchParams, ENDPOINT_SIMILAR_PRODUCTS_SEARCH);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    /**
     * Extract feature string (encoded in base 64) given an image file or url.
     *
     * @param uploadSearchParams the upload search parameters, must contain a image file or a url
     * @return the feature response string result
     */
    @Override
    public FeatureResponseResult extractFeature(UploadSearchParams uploadSearchParams) {
        try {
            ViSearchHttpResponse response = getPostImageSearchHttpResponse(uploadSearchParams, ENDPOINT_EXTRACT_FEATURE);
            return getFeatureResponseResult(response);
        } catch (InternalViSearchException e) {
            return new FeatureResponseResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    /**
     * Perform upload search by image
     * @param uploadSearchParams
     * @return
     */
    private PagedSearchResult postImageSearch(UploadSearchParams uploadSearchParams, String endpointMethod) {
        ViSearchHttpResponse response = getPostImageSearchHttpResponse(uploadSearchParams, endpointMethod);
        return getPagedResult(response);
    }

    private ViSearchHttpResponse getPostImageSearchHttpResponse(UploadSearchParams uploadSearchParams, String endpointMethod) {
        File imageFile = uploadSearchParams.getImageFile();
        InputStream imageStream = uploadSearchParams.getImageStream();
        String imageUrl = uploadSearchParams.getImageUrl();
        ViSearchHttpResponse response;

        // if im_id is available no need to check for image
        if(!Strings.isNullOrEmpty(uploadSearchParams.getImFeature())){
            response = viSearchHttpClient.postImFeature(endpointMethod, uploadSearchParams.toMap(), uploadSearchParams.getImFeature() , uploadSearchParams.getTransId() );
        } else if (!Strings.isNullOrEmpty(uploadSearchParams.getImId())){
            response = viSearchHttpClient.post(endpointMethod, uploadSearchParams.toMap());
        } else if (imageFile == null && imageStream == null && (Strings.isNullOrEmpty(imageUrl))) {
            throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_SOURCE);
            // throw new IllegalArgumentException("Must provide either an image File, InputStream of the image, or a valid image url to perform upload search");
        } else if (imageFile != null) {
            try {
                response = viSearchHttpClient.postImage(endpointMethod, uploadSearchParams.toMap(), new FileInputStream(imageFile), imageFile.getName());
            } catch (FileNotFoundException e) {
                throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_OR_URL, e);
                // throw new IllegalArgumentException("Could not open the image file.", e);
            }
        } else if (imageStream != null) {
            response = viSearchHttpClient.postImage(endpointMethod, uploadSearchParams.toMap(), imageStream, ViSearchHttpConstants.IMAGE_STREAM);
        } else {
            response = viSearchHttpClient.post(endpointMethod, uploadSearchParams.toMap());
        }
        return response;
    }

    private FeatureResponseResult getFeatureResponseResult(ViSearchHttpResponse httpResponse){
        String response = httpResponse.getBody();
        Map<String, String> headers = httpResponse.getHeaders();
        JsonNode node;
        try {
            node = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
            // throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
            // throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        }
        checkResponseStatus(node);

        FeatureResponseResult result = deserializeFeatureResponseResult(response, node);
        JsonNode imIdNode = node.get(ViSearchHttpConstants.IM_ID);
        if (imIdNode != null) {
            result.setImId(imIdNode.asText());
        }
        result.setRawJson(node.toString());
        result.setHeaders(headers);
        return result;
    }

    private PagedSearchResult getPagedResult(ViSearchHttpResponse httpResponse) {
        String response = httpResponse.getBody();
        Map<String, String> headers = httpResponse.getHeaders();
        JsonNode node;
        try {
            node = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
            // throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, response);
            // throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        }
        checkResponseStatus(node);

        PagedSearchResult result = pagify(response, node);

        JsonNode productTypesNode = node.get(ViSearchHttpConstants.PRODUCT_TYPES);
        if (productTypesNode != null) {
            List<ProductType> productTypes = deserializeListResult(response, productTypesNode, ProductType.class);
            result.setProductTypes(productTypes);
        }
        JsonNode productTypesListNode = node.get(ViSearchHttpConstants.PRODUCT_TYPES_LIST);
        if (productTypesListNode != null) {
            List<ProductType> productTypesList = deserializeListResult(response, productTypesListNode, ProductType.class);
            result.setProductTypesList(productTypesList);
        }
        JsonNode objectTypesListNode = node.get(ViSearchHttpConstants.OBJECT_TYPES_LIST);
        if (objectTypesListNode != null) {
            List<ProductType> objectTypesList = deserializeListResult(response, objectTypesListNode, ProductType.class);
            result.setObjectTypesList(objectTypesList);
        }
        JsonNode imIdNode = node.get(ViSearchHttpConstants.IM_ID);
        if (imIdNode != null) {
            result.setImId(imIdNode.asText());
        }
        JsonNode facetsNode = node.get(ViSearchHttpConstants.FACETS);
        if (facetsNode != null) {
            List<Facet> facets = deserializeListResult(response, facetsNode, Facet.class);
            result.setFacets(facets);
        }
        JsonNode qinfoNode = node.get(ViSearchHttpConstants.QINFO);
        if (qinfoNode != null) {
            Map<String, String> qinfo = deserializeMapResult(response, qinfoNode, String.class, String.class);
            result.setQueryInfo(qinfo);
        }
        // For similarproducts search, try to cover it's result into discoversearch result.
        JsonNode groupResult = node.get(ViSearchHttpConstants.GROUP_RESULT);
        if (groupResult != null && groupResult instanceof ArrayNode) {
            List<ProductType> productTypes = result.getProductTypes();
            List<ObjectSearchResult> objects = Lists.newArrayList();
            ArrayNode arrayNode = (ArrayNode) groupResult;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode oneGroup = arrayNode.get(i);
                ProductType productType = productTypes.get(i);
                ObjectSearchResult objectSearchResult = new ObjectSearchResult();
                objectSearchResult.setResult(deserializeListResult(response, oneGroup, ImageResult.class));
                objectSearchResult.setScore(productType.getScore());
                objectSearchResult.setAttributes(productType.getAttributes());
                objectSearchResult.setAttributesList(productType.getAttributesList());
                objectSearchResult.setBox(productType.getBox());
                objectSearchResult.setType(productType.getType());
                objects.add(objectSearchResult);
            }
            result.setObjects(objects);
            result.setObjectTypesList(result.getProductTypesList());
        }

        result.setRawJson(node.toString());
        result.setHeaders(headers);
        return result;
    }

    private static void checkResponseStatus(JsonNode node) {
        String json = node.toString();
        JsonNode statusNode = node.get(ViSearchHttpConstants.STATUS);
        if (statusNode == null) {
            throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, json);
            // throw new ViSearchException("There was a malformed ViSearch response: " + json, json);
        } else {
            String status = statusNode.asText();
            if (!ViSearchHttpConstants.OK.equals(status)) {
                JsonNode errorNode = node.get(ViSearchHttpConstants.ERROR);
                if (errorNode == null) {
                    throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, json);
                    // throw new ViSearchException("An unknown error occurred in ViSearch: " + json, json);
                }
                String message = errorNode.path(0).asText();
                throw new InternalViSearchException(message, json);
                // throw new ViSearchException("An error occurred calling ViSearch: " + message, json);
            }
        }
    }
}
