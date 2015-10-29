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

    public SearchOperationsImpl(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper) {
        super(viSearchHttpClient, objectMapper);
    }

    @Override
    public PagedSearchResult search(SearchParams searchParams) {
        ViSearchHttpResponse response = viSearchHttpClient.get("/search", searchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult colorSearch(ColorSearchParams colorSearchParams) {
        ViSearchHttpResponse response = viSearchHttpClient.get("/colorsearch", colorSearchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams) {
        return uploadSearchInternal(uploadSearchParams);
    }

    @Deprecated
    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings) {
        return uploadSearchInternal(uploadSearchParams);
    }

    private PagedSearchResult uploadSearchInternal(UploadSearchParams uploadSearchParams) {
        File imageFile = uploadSearchParams.getImageFile();
        InputStream imageStream = uploadSearchParams.getImageStream();
        String imageUrl = uploadSearchParams.getImageUrl();
        ViSearchHttpResponse response;
        if (imageFile == null && imageStream == null && (Strings.isNullOrEmpty(imageUrl))) {
            throw new IllegalArgumentException("Must provide either an image File, InputStream of the image, or a valid image url to perform upload search");
        } else if (imageFile != null) {
            try {
                response = viSearchHttpClient.postImage("/uploadsearch", uploadSearchParams.toMap(), new FileInputStream(imageFile), imageFile.getName());
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Could not open the image file.", e);
            }
        } else if (imageStream != null) {
            response = viSearchHttpClient.postImage("/uploadsearch", uploadSearchParams.toMap(), imageStream, "image-stream");
        } else {
            response = viSearchHttpClient.post("/uploadsearch", uploadSearchParams.toMap());
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
            throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        } catch (IOException e) {
            throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        }
        checkResponseStatus(node);

        PagedResult<ImageResult> pagedResult = pagify(response, ImageResult.class);
        PagedSearchResult result = new PagedSearchResult(pagedResult);
        JsonNode facetsNode = node.get("facets");
        if (facetsNode != null) {
            List<Facet> facets = deserializeListResult(facetsNode, Facet.class);
            result.setFacets(facets);
        }
        JsonNode qinfoNode = node.get("qinfo");
        if (qinfoNode != null) {
            Map<String, String> qinfo = deserializeMapResult(qinfoNode, String.class, String.class);
            result.setQueryInfo(qinfo);
        }
        result.setRawJson(node.toString());
        result.setHeaders(headers);
        return result;
    }

    private void checkResponseStatus(JsonNode node) {
        String json = node.toString();
        JsonNode statusNode = node.get("status");
        if (statusNode == null) {
            throw new ViSearchException("There was a malformed ViSearch response: " + json, json);
        } else {
            String status = statusNode.asText();
            if (!status.equals("OK")) {
                JsonNode errorNode = node.get("error");
                if (errorNode == null) {
                    throw new ViSearchException("An unknown error occurred in ViSearch: " + json, json);
                }
                String message = errorNode.path(0).asText();
                throw new ViSearchException("An error occurred calling ViSearch: " + message, json);
            }
        }
    }
}
