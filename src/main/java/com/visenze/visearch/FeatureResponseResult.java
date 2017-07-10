package com.visenze.visearch;

import java.util.List;
import com.visenze.visearch.internal.ResponseBase;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;

/**
 * Created by Hung on 10/7/17.
 */
public class FeatureResponseResult extends ResponseBase {

    private String imId;

    private String rawJson;

    private List<String> result;

    public FeatureResponseResult(List<String> result) {
        this.result = result;
    }

    public FeatureResponseResult(String errorMessage, Throwable e, String rawResponse) {
        super.setErrorMessage(errorMessage);
        super.setCause(e);
        super.setRawResponseMessage(rawResponse);
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getImId() {
        return imId;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    public String getRawJson() {
        return rawJson;
    }

    /**
     * Get the request id to identify this request.
     */
    public String getReqId(){
        if(this.headers!=null && this.headers.containsKey(ViSearchHttpConstants.X_LOG_ID)){
            return headers.get(ViSearchHttpConstants.X_LOG_ID);
        }
        return ViSearchHttpConstants.X_LOG_ID_EMPTY;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public List<String> getResult() { return result; }

}
