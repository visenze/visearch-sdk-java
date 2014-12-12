package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

import java.io.File;
import java.util.List;
import java.util.Map;

class SearchOperationsImpl extends AbstractViSenzeOperations implements SearchOperations {

    public SearchOperationsImpl(ViSearchHttpClient viSearchHttpClient, String endpoint) {
        super(viSearchHttpClient, endpoint);
    }

    @Override
    public PagedSearchResult<ImageResult> search(SearchParams searchParams) {
        String imageId = searchParams.getImName();
        if (imageId == null || imageId.isEmpty()) {
            throw new ViSearchException("Missing parameter");
        }
        JsonNode node = viSearchHttpClient.getForObject(endpoint + "/search", searchParams.toMap(), JsonNode.class);
        return getResult(node);
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
        JsonNode node = viSearchHttpClient.getForObject(endpoint + "/colorsearch", colorSearchParams.toMap(), JsonNode.class);
        return getResult(node);
    }

    @Override
    public PagedSearchResult<ImageResult> uploadSearch(UploadSearchParams uploadSearchParams) {
        File imageFile = uploadSearchParams.getImageFile();
        byte[] imageBytes = uploadSearchParams.getImageBytes();
        String imageUrl = uploadSearchParams.getImageUrl();
        JsonNode node;
        if (imageFile == null && imageBytes == null && (imageUrl == null || imageUrl.isEmpty())) {
            throw new ViSearchException("Missing parameter");
        } else if (imageFile != null) {
            node = viSearchHttpClient.postForObject(endpoint + "/uploadsearch", uploadSearchParams.toMap(), imageFile, JsonNode.class);
        } else if (imageBytes != null) {
            node = viSearchHttpClient.postForObject(endpoint + "/uploadsearch", uploadSearchParams.toMap(), imageBytes, JsonNode.class);
        } else {
            node = viSearchHttpClient.postForObject(endpoint + "/uploadsearch", uploadSearchParams.toMap(), JsonNode.class);
        }
        return getResult(node);
    }

    private PagedSearchResult<ImageResult> getResult(JsonNode node) {
        checkStatus(node);
        PagedResult<ImageResult> pagedResult = pagify(node, ImageResult.class);
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
