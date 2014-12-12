package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.visenze.visearch.InsertTransaction;
import com.visenze.visearch.ViSearchException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class InsertTransactionDeserializer extends JsonDeserializer<InsertTransaction> {

    @Override
    public InsertTransaction deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAs(JsonNode.class);
        if (null == node || node.isMissingNode() || node.isNull()) {
            return null;
        }
        return this.deserialize(node);
    }

    public InsertTransaction deserialize(JsonNode node) throws IOException {
        String tid;
        Integer total;
        Integer failCount;
        Integer successCount;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date startTime = null;
        Date updateTime = null;
        JsonNode resultNode = node.path("result").path(0);
        try {
            if (!resultNode.isMissingNode()) {
                tid = resultNode.path("trans_id").asText();
                total = resultNode.path("total").asInt();
                failCount = resultNode.path("fail_count").asInt();
                successCount = resultNode.path("success_count").asInt();
                try {
                    startTime = sdf.parse(resultNode.path("start_time").asText());
                    updateTime = sdf.parse(resultNode.path("update_time").asText());
                } catch (ParseException e) {
                    throw new IOException("Cannot parse date");
                }
            } else {
                tid = node.path("trans_id").asText();
                total = node.path("total").asInt();
                failCount = node.path("fail_count").asInt();
                successCount = node.path("success_count").asInt();
                String startTimeStr = node.path("start_time").asText();
                String endTimeStr = node.path("update_time").asText();
                try {
                    if (null != startTimeStr && !startTimeStr.isEmpty()) {
                        startTime = sdf.parse(startTimeStr);
                    }
                    if (null != endTimeStr && !endTimeStr.isEmpty()) {
                        updateTime = sdf.parse(endTimeStr);
                    }
                } catch (ParseException e) {
                    throw new IOException("Cannot parse date");
                }
            }
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing InsertTransaction");
        }
        return new InsertTransaction(tid, total, failCount, successCount, startTime, updateTime);
    }
}
