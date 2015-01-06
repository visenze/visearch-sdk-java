package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.visenze.visearch.Image;
import com.visenze.visearch.InsertTransaction;
import com.visenze.visearch.PagedResult;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOperationsImpl extends BaseViSearchOperations implements DataOperations {

    public DataOperationsImpl(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper, String endpoint) {
        super(viSearchHttpClient, objectMapper, endpoint);
    }

    @Override
    public InsertTransaction insert(List<Image> imageList) {
        Multimap<String, String> params = Multimaps.forMap(imageListToParams(imageList));
        String response = viSearchHttpClient.post(endpoint + "/insert", params);
        return deserializeObjectResult(response, InsertTransaction.class);
    }

    private Map<String, String> imageListToParams(List<Image> imageList) {
        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < imageList.size(); i++) {
            Image image = imageList.get(0);
            if (image != null) {
                params.put("im_name" + "[" + i + "]", image.getImName());
                params.put("im_url" + "[" + i + "]", image.getImUrl());
                Map<String, String> fields = image.getFields();
                if (fields != null) {
                    for (Map.Entry<String, String> entry : fields.entrySet()) {
                        params.put(entry.getKey() + "[" + i + "]", entry.getValue());
                    }
                }
            }
        }
        return params;
    }

    @Override
    public PagedResult<InsertTransaction> getStatus(Integer page, Integer limit) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        Multimap<String, String> params = HashMultimap.create();
        params.put("page", page.toString());
        params.put("limit", limit.toString());

        String response = viSearchHttpClient.get(endpoint + "/insert/status", params);
        return pagify(response, InsertTransaction.class);
    }

    @Override
    public InsertTransaction getStatus(String transactionId) {
        String response = viSearchHttpClient.get(endpoint + "/insert/status/" + transactionId, null);
        return deserializeObjectResult(response, InsertTransaction.class);
    }

    @Override
    public void remove(List<Image> imageList) {
        Multimap<String, String> params = Multimaps.forMap(imageListToParams(imageList));
        viSearchHttpClient.post(endpoint + "/remove", params);
    }

}
