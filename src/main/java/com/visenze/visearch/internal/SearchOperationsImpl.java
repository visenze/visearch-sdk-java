package com.visenze.visearch.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchOperationsImpl extends BaseViSearchOperations implements SearchOperations {

    private static final String ENDPOINT_DISCOVER_SEARCH = "/discoversearch";
    private static final String ENDPOINT_UPLOAD_SEARCH = "/uploadsearch";
    private static final String ENDPOINT_MULTI_SEARCH = "/multisearch";
    private static final String ENDPOINT_MULTI_SEARCH_AUTOCOMPLETE = "/multisearch/autocomplete";

    private static final String ENDPOINT_SEARCH = "/search";
    private static final String ENDPOINT_RECOMMENDATION = "/recommendations";
    private static final String ENDPOINT_COLOR_SEARCH = "/colorsearch";
    private static final String ENDPOINT_SIMILAR_PRODUCTS_SEARCH = "/similarproducts";
    private static final String ENDPOINT_EXTRACT_FEATURE= "/extractfeature";
    private static final String ENDPOINT_MATCH= "/match";

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
    public PagedSearchResult recommendation(RecommendSearchParams recommendSearchParams) {
        try {
            ViSearchHttpResponse response = viSearchHttpClient.post(ENDPOINT_RECOMMENDATION, recommendSearchParams.toMap());
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

    @Override
    public PagedSearchResult multiSearch(UploadSearchParams uploadSearchParams) {
        try {
            return postImageSearch(uploadSearchParams, ENDPOINT_MULTI_SEARCH);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    @Override
    public AutoCompleteResult multiSearchAutoComplete(UploadSearchParams uploadSearchParams) {
        try {
            return postMultiSearchAutoComplete(uploadSearchParams);
        } catch (InternalViSearchException e) {
            return new AutoCompleteResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    /**
     * Perform real discover search
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
     * Perform real discover search
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


    @Override
    public PagedSearchResult matchSearch(MatchSearchParams matchSearchParams) {
        try {
            ViSearchHttpResponse response = viSearchHttpClient.get(ENDPOINT_MATCH, matchSearchParams.toMap());
            return getPagedResult(response);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
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

    private AutoCompleteResult postMultiSearchAutoComplete(UploadSearchParams uploadSearchParams) {
        ViSearchHttpResponse response = getPostImageSearchHttpResponse(uploadSearchParams, ENDPOINT_MULTI_SEARCH_AUTOCOMPLETE);
        return getAutoCompleteResult(response);
    }

    private ViSearchHttpResponse getPostImageSearchHttpResponse(UploadSearchParams uploadSearchParams, String endpointMethod) {
        File imageFile = uploadSearchParams.getImageFile();
        InputStream imageStream = uploadSearchParams.getImageStream();
        String imageUrl = uploadSearchParams.getImageUrl();

        boolean isMultiSearch = ENDPOINT_MULTI_SEARCH.equals(endpointMethod) || ENDPOINT_MULTI_SEARCH_AUTOCOMPLETE.equals(endpointMethod);

        // if im_id is available no need to check for image
        if(!Strings.isNullOrEmpty(uploadSearchParams.getImFeature())){
            return viSearchHttpClient.postImFeature(endpointMethod, uploadSearchParams.toMap(), uploadSearchParams.getImFeature() , uploadSearchParams.getTransId() );
        }

        if (!Strings.isNullOrEmpty(uploadSearchParams.getImId())){
            return viSearchHttpClient.post(endpointMethod, uploadSearchParams.toMap());
        }

        boolean isImageMissing = imageFile == null && imageStream == null && Strings.isNullOrEmpty(imageUrl);
        if (isImageMissing && (!isMultiSearch)) {
            throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_SOURCE);
            // throw new IllegalArgumentException("Must provide either an image File, InputStream of the image, or a valid image url to perform upload search");
        }

        if (imageFile != null) {
            try {
                return viSearchHttpClient.postImage(endpointMethod, uploadSearchParams.toMap(), new FileInputStream(imageFile), imageFile.getName());
            } catch (FileNotFoundException e) {
                throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_OR_URL, e);
                // throw new IllegalArgumentException("Could not open the image file.", e);
            }
        }

        if (imageStream != null) {
            return viSearchHttpClient.postImage(endpointMethod, uploadSearchParams.toMap(), imageStream, ViSearchHttpConstants.IMAGE_STREAM);
        }

        return viSearchHttpClient.post(endpointMethod, uploadSearchParams.toMap());
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
        JsonNode reqidNode = node.get(ViSearchHttpConstants.REQID);
        if (reqidNode != null) {
            result.setReqId(reqidNode.asText());
        }
        result.setRawJson(node.toString());
        result.setHeaders(headers);
        return result;
    }

    private AutoCompleteResult getAutoCompleteResult(ViSearchHttpResponse httpResponse) {
        String rawResponse = httpResponse.getBody();
        Map<String, String> headers = httpResponse.getHeaders();
        JsonNode node;

        try {
            node = objectMapper.readTree(rawResponse);
        } catch (JsonProcessingException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
        } catch (IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e, rawResponse);
        }
        checkResponseStatus(node);

        List<AutoCompleteResultItem> result = new ArrayList<AutoCompleteResultItem>();

        if(node.has(ViSearchHttpConstants.RESULT)) {
            result = deserializeListResult(rawResponse, node.get(ViSearchHttpConstants.RESULT), AutoCompleteResultItem.class);
        }

        AutoCompleteResult autoCompleteResult = new AutoCompleteResult(result);

        JsonNode pageNode = node.get(ViSearchHttpConstants.PAGE);
        JsonNode limitNode = node.get(ViSearchHttpConstants.LIMIT);
        JsonNode totalNode = node.get(ViSearchHttpConstants.TOTAL);

        if(pageNode != null) {
            autoCompleteResult.setPage(pageNode.asInt());
        }

        if(limitNode != null) {
            autoCompleteResult.setLimit(limitNode.asInt());
        }

        if(totalNode != null) {
            autoCompleteResult.setTotal(totalNode.asInt());
        }

        JsonNode reqidNode = node.get(ViSearchHttpConstants.REQID);
        if (reqidNode != null) {
            autoCompleteResult.setReqId(reqidNode.asText());
        }

        autoCompleteResult.setRawJson(node.toString());
        autoCompleteResult.setHeaders(headers);
        return autoCompleteResult;
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
        JsonNode reqidNode = node.get(ViSearchHttpConstants.REQID);
        if (reqidNode != null) {
            result.setReqId(reqidNode.asText());
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

        JsonNode sysInfoNode = node.get(ViSearchHttpConstants.Q_VS_META_INFO);
        if (sysInfoNode != null) {
            Map<String, String> sysInfo = deserializeMapResult(response, sysInfoNode, String.class, String.class);
            result.setSysQueryInfo(sysInfo);
        }

        JsonNode queryBestImagesNode = node.get(ViSearchHttpConstants.Q_BEST_IMAGES);
        if (queryBestImagesNode != null) {
            List<BestImage> bestImageList = deserializeListResult(response, queryBestImagesNode, BestImage.class);
            result.setQueryBestImages(bestImageList);
        }

        JsonNode excludedImNamesNode = node.get(ViSearchHttpConstants.EXCLUDED_IM_NAMES);
        if (excludedImNamesNode != null) {
            List<String> excludedImNames = deserializeListResult(response, excludedImNamesNode, String.class);
            result.setExcludedImNames(excludedImNames);
        }

        JsonNode hiddenCategoriesNode = node.get(ViSearchHttpConstants.HIDDEN_CATEGORIES);
        if (hiddenCategoriesNode != null) {
            List<String> hiddenCategories = deserializeListResult(response, hiddenCategoriesNode, String.class);
            result.setHiddenCategories(hiddenCategories);
        }

        JsonNode setInfoListNode = node.get(ViSearchHttpConstants.SET_INFO);
        if (setInfoListNode != null) {
            List<SetInfo> setInfoList = deserializeListResult(response, setInfoListNode, SetInfo.class);
            result.setSetInfoList(setInfoList);
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
