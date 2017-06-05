package com.visenze.visearch;

import java.util.List;
import java.util.Map;

public class PagedSearchResult extends PagedResult<ImageResult> {

    private static final String X_LOG_ID = "X-Log-ID";
    
    private static final String X_LOG_ID_EMPTY = "";

    private List<ProductType> productTypes;

    private List<ProductType> productTypesList;

    private String imId;

    private List<Facet> facets;

    private Map<String, String> queryInfo;

    private String rawJson;

    private List<ObjectSearchResult> objects;

    private List<ProductType> objectTypesList;

    public PagedSearchResult(Integer page, Integer limit, Integer total, List<ImageResult> result) {
        this.page = page;
        this.limit = limit;
        this.total = total;
        this.result = result;
    }

    public PagedSearchResult(PagedResult<ImageResult> pagedResult) {
        super(pagedResult.getPage(), pagedResult.getLimit(), pagedResult.getTotal(), pagedResult.getResult());
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
        if(this.headers!=null && this.headers.containsKey(X_LOG_ID)){
            return headers.get(X_LOG_ID);
        }
        return X_LOG_ID_EMPTY;
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
}
