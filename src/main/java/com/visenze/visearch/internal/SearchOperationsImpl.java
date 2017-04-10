package com.visenze.visearch.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;

import java.io.*;
import java.util.List;
import java.util.Map;

public class SearchOperationsImpl extends BaseViSearchOperations implements SearchOperations {

    private static final String ENDPOINT_UPLOAD_SEARCH = "/uploadsearch";
    private static final String ENDPOINT_SEARCH = "/search";
    private static final String ENDPOINT_RECOMMENDATION = "/recommendation";
    private static final String ENDPOINT_COLOR_SEARCH = "/colorsearch";

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
            return uploadSearchInternal(uploadSearchParams);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    /**
     * @deprecated
     * */
    @Deprecated
    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings) {
        try {
            return uploadSearchInternal(uploadSearchParams);
        } catch (InternalViSearchException e) {
            return new PagedSearchResult(e.getMessage(), e.getCause(), e.getServerRawResponse());
        }
    }

    private PagedSearchResult uploadSearchInternal(UploadSearchParams uploadSearchParams) {
        File imageFile = uploadSearchParams.getImageFile();
        InputStream imageStream = uploadSearchParams.getImageStream();
        String imageUrl = uploadSearchParams.getImageUrl();
        ViSearchHttpResponse response;

        // if im_id is available no need to check for image
        if (!Strings.isNullOrEmpty(uploadSearchParams.getImId())){
            response = viSearchHttpClient.post(ENDPOINT_UPLOAD_SEARCH, uploadSearchParams.toMap());
        }
        else if (imageFile == null && imageStream == null && (Strings.isNullOrEmpty(imageUrl))) {
            throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_SOURCE);
            // throw new IllegalArgumentException("Must provide either an image File, InputStream of the image, or a valid image url to perform upload search");
        } else if (imageFile != null) {
            try {
                response = viSearchHttpClient.postImage(ENDPOINT_UPLOAD_SEARCH, uploadSearchParams.toMap(), new FileInputStream(imageFile), imageFile.getName());
            } catch (FileNotFoundException e) {
                throw new InternalViSearchException(ResponseMessages.INVALID_IMAGE_OR_URL, e);
                // throw new IllegalArgumentException("Could not open the image file.", e);
            }
        } else if (imageStream != null) {
            response = viSearchHttpClient.postImage(ENDPOINT_UPLOAD_SEARCH, uploadSearchParams.toMap(), imageStream, "image-stream");
        } else {
            response = viSearchHttpClient.post(ENDPOINT_UPLOAD_SEARCH, uploadSearchParams.toMap());
        }
        return getPagedResult(response);
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

        PagedResult<ImageResult> pagedResult = pagify(response, response, ImageResult.class);
        PagedSearchResult result = new PagedSearchResult(pagedResult);
        JsonNode productTypesNode = node.get("product_types");
        if (productTypesNode != null) {
            List<ProductType> productTypes = deserializeListResult(response, productTypesNode, ProductType.class);
            result.setProductTypes(productTypes);
        }
        JsonNode productTypesListNode = node.get("product_types_list");
        if (productTypesListNode != null) {
            List<ProductType> productTypesList = deserializeListResult(response, productTypesListNode, ProductType.class);
            result.setProductTypesList(productTypesList);
        }
        JsonNode imIdNode = node.get("im_id");
        if (imIdNode != null) {
            result.setImId(imIdNode.asText());
        }
        JsonNode facetsNode = node.get("facets");
        if (facetsNode != null) {
            List<Facet> facets = deserializeListResult(response, facetsNode, Facet.class);
            result.setFacets(facets);
        }
        JsonNode qinfoNode = node.get("qinfo");
        if (qinfoNode != null) {
            Map<String, String> qinfo = deserializeMapResult(response, qinfoNode, String.class, String.class);
            result.setQueryInfo(qinfo);
        }
        result.setRawJson(node.toString());
        result.setHeaders(headers);
        return result;
    }

    private static void checkResponseStatus(JsonNode node) {
        String json = node.toString();
        JsonNode statusNode = node.get("status");
        if (statusNode == null) {
            throw new InternalViSearchException(ResponseMessages.INVALID_RESPONSE_FORMAT, json);
            // throw new ViSearchException("There was a malformed ViSearch response: " + json, json);
        } else {
            String status = statusNode.asText();
            if (!"OK".equals(status)) {
                JsonNode errorNode = node.get("error");
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
