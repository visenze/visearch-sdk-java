package com.visenze.visearch;

import java.util.List;
import java.util.Map;

public class PagedSearchGroupResult extends PagedResult<GroupImageResult> {

    private static final String X_LOG_ID = "X-Log-ID";

    private static final String X_LOG_ID_EMPTY = "";

    private List<ProductType> productTypes;

    private List<ProductType> productTypesList;

    private String imId;

    private Map<String, String> queryInfo;

    private String rawJson;

    public PagedSearchGroupResult(PagedResult<GroupImageResult> pagedResult) {
        super(pagedResult.getPage(), pagedResult.getLimit(), pagedResult.getTotal(), pagedResult.getResult());
    }

    public PagedSearchGroupResult(String errorMessage, Throwable e, String rawResponse) {
        super.setErrorMessage(errorMessage);
        super.setCause(e);
        super.setRawResponseMessage(rawResponse);
    }

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

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getImId() {
        return imId;
    }

    public void setQueryInfo(Map<String, String> queryInfo) {
        this.queryInfo = queryInfo;
    }

    public Map<String, String> getQueryInfo() {
        return queryInfo;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    public String getRawJson() {
        return rawJson;
    }

    /**
     * Get the request id to identify this request.
     * @return
     */
    public String getReqId(){
        if(this.headers!=null && this.headers.containsKey(X_LOG_ID)){
            return headers.get(X_LOG_ID);
        }
        return X_LOG_ID_EMPTY;
    }
}
