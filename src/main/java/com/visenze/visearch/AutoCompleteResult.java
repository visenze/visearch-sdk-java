package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Created by Hung on 24/11/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoCompleteResult extends PagedResult<AutoCompleteResultItem> {

    private String reqId;

    private String rawJson;

    public AutoCompleteResult(List<AutoCompleteResultItem> result) {
        this.result = result;
    }

    public AutoCompleteResult(String errorMessage, Throwable e, String rawResponse) {
        super.setErrorMessage(errorMessage);
        super.setCause(e);
        super.setRawResponseMessage(rawResponse);
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getRawJson() {
        return rawJson;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }
}
