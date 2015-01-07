package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SearchOperationsImpl extends BaseViSearchOperations implements SearchOperations {

    public SearchOperationsImpl(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper, String endpoint) {
        super(viSearchHttpClient, objectMapper, endpoint);
    }

    @Override
    public PagedSearchResult<ImageResult> search(SearchParams searchParams) {
        String imageId = searchParams.getImName();
        if (imageId == null || imageId.isEmpty()) {
            throw new ViSearchException("Missing parameter");
        }
        String response = viSearchHttpClient.get(endpoint + "/search", searchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult<ImageResult> colorSearch(ColorSearchParams colorSearchParams) {
        String color = colorSearchParams.getColor();
        if (color == null || color.isEmpty()) {
            throw new ViSearchException("Missing parameter");
        }
        if (!color.matches("^[0-9a-fA-F]{6}$")) {
            throw new ViSearchException("Invalid parameter");
        }
        String response = viSearchHttpClient.get(endpoint + "/colorsearch", colorSearchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult<ImageResult> uploadSearch(UploadSearchParams uploadSearchParams) {
        File imageFile = uploadSearchParams.getImageFile();
        byte[] imageBytes = uploadSearchParams.getImageBytes();
        String imageUrl = uploadSearchParams.getImageUrl();
        String response;
        if (imageFile == null && imageBytes == null && (imageUrl == null || imageUrl.isEmpty())) {
            throw new ViSearchException("Missing image parameter for upload search");
        } else if (imageFile != null) {
            response = viSearchHttpClient.postImage(endpoint + "/uploadsearch", uploadSearchParams.toMap(), imageFile);
        } else if (imageBytes != null) {
            response = viSearchHttpClient.postImage(endpoint + "/uploadsearch", uploadSearchParams.toMap(), imageBytes);
        } else {
            response = viSearchHttpClient.post(endpoint + "/uploadsearch", uploadSearchParams.toMap());
        }
        return getPagedResult(response);
    }

    private PagedSearchResult<ImageResult> getPagedResult(String json) {
        JsonNode node;
        try {
            node = objectMapper.readTree(json);
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing json=" + json);
        }
        checkStatus(node);
        PagedResult<ImageResult> pagedResult = pagify(json, ImageResult.class);
        PagedSearchResult<ImageResult> result = new PagedSearchResult<ImageResult>(pagedResult);
        JsonNode facetsNode = node.get("facets");
        if (facetsNode != null) {
            List<Facet> facets = deserializeListResult(facetsNode, Facet.class);
            result.setFacets(facets);
        }
        JsonNode qinfoNode = node.get("qinfo");
        if (qinfoNode != null) {
            try {
                Map<String, String> qinfo = deserializeMapResult(qinfoNode, String.class, String.class);
                result.setQueryInfo(qinfo);
            } catch (Exception e) {
                throw new ViSearchException("Error deserializing qinfo");
            }
        }
        result.setRawJson(node.toString());
        return result;
    }

    private void checkStatus(JsonNode node) {
        JsonNode statusNode = node.get("status");
        if (statusNode == null) {
            throw new ViSearchException("Error receiving api response");
        } else {
            String status = statusNode.asText();
            if (!status.equals("OK")) {
                JsonNode errorNode = node.get("error");
                if (errorNode == null) {
                    throw new ViSearchException("Error receiving api response");
                }
                String errorMessage = errorNode.path(0).asText();
                throw new ViSearchException("Error : " + errorMessage, node.toString());
            }
        }
    }
}
