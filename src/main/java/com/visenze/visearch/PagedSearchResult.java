package com.visenze.visearch;

import com.visenze.visearch.internal.constant.ViSearchHttpConstants;

import java.util.List;
import java.util.Map;

public class PagedSearchResult extends PagedResult<ImageResult> {

    private List<ProductType> productTypes;

    private List<ProductType> productTypesList;

    private String imId;

    private List<Facet> facets;

    private Map<String, String> queryInfo;

    private String rawJson;

    private List<ObjectSearchResult> objects;

    private List<ProductType> objectTypesList;

    private List<GroupSearchResult> groupSearchResults;

    public PagedSearchResult(List<ImageResult> result) {
        this.result = result;
    }

    public PagedSearchResult(String errorMessage, Throwable e, String rawResponse) {
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

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public List<Facet> getFacets() {
        return facets;
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
     */
    public String getReqId(){
        if(this.headers!=null && this.headers.containsKey(ViSearchHttpConstants.X_LOG_ID)){
            return headers.get(ViSearchHttpConstants.X_LOG_ID);
        }
        return ViSearchHttpConstants.X_LOG_ID_EMPTY;
    }

    /**
     * Get discoversearch(multiple product search) object result list.
     * Available only in {@link ViSearch#discoverSearch(UploadSearchParams)}
     * {@link ViSearch#discoverSearch(UploadSearchParams)}
     * @return detected object and search result
     */
    public List<ObjectSearchResult> getObjects() {
        return objects;
    }

    /**
     * Set discoversearch(multiple product search) object result list.
     * Available only in {@link ViSearch#discoverSearch(UploadSearchParams)}
     * {@link ViSearch#discoverSearch(UploadSearchParams)}
     * @param objects
     */
    public void setObjects(List<ObjectSearchResult> objects) {
        this.objects = objects;
    }

    public List<ProductType> getObjectTypesList() {
        return objectTypesList;
    }

    public void setObjectTypesList(List<ProductType> objectTypesList) {
        this.objectTypesList = objectTypesList;
    }

    public List<GroupSearchResult> getGroupSearchResults() { return groupSearchResults; }

    public void setGroupSearchResults(List<GroupSearchResult> groupSearchResults) { this.groupSearchResults = groupSearchResults; }
}
