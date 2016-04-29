package com.visenze.visearch;

import java.util.List;
import java.util.Map;

public class PagedSearchResult extends PagedResult<ImageResult> {

    private List<ProductType> productTypes;

    private List<ProductType> productTypesList;

    private String imId;

    private List<Facet> facets;

    private Map<String, String> queryInfo;

    private String rawJson;

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
}
