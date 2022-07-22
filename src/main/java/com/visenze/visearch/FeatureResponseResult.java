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

    private List<ProductType> productTypesList;

    private List<ProductType> productTypes;

    private String reqId;

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
     * @return request ID
     */
    public String getReqId(){
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public List<String> getResult() { return result; }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypesList(List<ProductType> productTypesList) {
        this.productTypesList = productTypesList;
    }

    public List<ProductType> getProductTypesList() {
        return productTypesList;
    }

}
