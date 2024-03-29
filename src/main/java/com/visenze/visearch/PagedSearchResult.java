package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedSearchResult extends PagedResult<ImageResult> {

    private List<ProductType> productTypes;

    private List<ProductType> productTypesList;

    private String imId;

    private List<Facet> facets;

    private Map<String, String> queryInfo;

    private List<BestImage> queryBestImages;

    private Map<String, String> sysQueryInfo;

    private String rawJson;

    private List<ObjectSearchResult> objects;

    private List<ProductType> objectTypesList;

    private List<GroupSearchResult> groupSearchResults;

    private String algorithm;

    private String fallbackAlgorithm;

    private List<String> excludedImNames;

    private List<String> hiddenCategories;

    private Map<String, String> pinCategoryPidMap;

    private List<SetInfo> setInfoList;

    private String reqId;

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

    public Map<String, String> getSysQueryInfo() {
        return sysQueryInfo;
    }

    public void setSysQueryInfo(Map<String, String> sysQueryInfo) {
        this.sysQueryInfo = sysQueryInfo;
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
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
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

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getFallbackAlgorithm() {
        return fallbackAlgorithm;
    }

    public void setFallbackAlgorithm(String fallbackAlgorithm) {
        this.fallbackAlgorithm = fallbackAlgorithm;
    }

    public List<String> getExcludedImNames() {
        return excludedImNames;
    }

    public void setExcludedImNames(List<String> excludedImNames) {
        this.excludedImNames = excludedImNames;
    }

    public List<String> getHiddenCategories() {
        return hiddenCategories;
    }

    public void setHiddenCategories(List<String> hiddenCategories) {
        this.hiddenCategories = hiddenCategories;
    }

    public Map<String, String> getPinCategoryPidMap() {
        return pinCategoryPidMap;
    }

    public void setPinCategoryPidMap(Map<String, String> pinCategoryPidMap) {
        this.pinCategoryPidMap = pinCategoryPidMap;
    }

    public List<SetInfo> getSetInfoList() {
        return setInfoList;
    }

    public void setSetInfoList(List<SetInfo> setInfoList) {
        this.setInfoList = setInfoList;
    }

    public List<BestImage> getQueryBestImages() {
        return queryBestImages;
    }

    public void setQueryBestImages(List<BestImage> queryBestImages) {
        this.queryBestImages = queryBestImages;
    }
}
