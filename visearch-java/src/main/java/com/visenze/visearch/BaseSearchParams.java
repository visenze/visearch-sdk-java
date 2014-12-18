package com.visenze.visearch;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.List;
import java.util.Map;

public class BaseSearchParams {

    protected Integer limit;

    protected Integer page;

    protected Boolean facet;

    protected Integer facetSize;

    protected Boolean score;

    protected Map<String, String> fq;

    protected List<String> fl;

    protected Boolean queryInfo;

    protected Map<String, String> custom;

    public BaseSearchParams() {
        this.limit = 10;
        this.page = 1;
        this.facet = null;
        this.facetSize = 10;
        this.score = null;
        this.fq = null;
        this.fl = null;
        this.queryInfo = null;
        this.custom = null;
    }

    public BaseSearchParams setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public BaseSearchParams setPage(Integer page) {
        this.page = page;
        return this;
    }

    public BaseSearchParams setFacet(Boolean facet) {
        this.facet = facet;
        return this;
    }

    public BaseSearchParams setFacetSize(Integer facetSize) {
        this.facetSize = facetSize;
        return this;
    }

    public BaseSearchParams setScore(Boolean score) {
        this.score = score;
        return this;
    }

    public BaseSearchParams setFq(Map<String, String> fq) {
        this.fq = fq;
        return this;
    }

    public BaseSearchParams setFl(List<String> fl) {
        this.fl = fl;
        return this;
    }

    public BaseSearchParams setQueryInfo(Boolean queryInfo) {
        this.queryInfo = queryInfo;
        return this;
    }

    public BaseSearchParams setCustom(Map<String, String> custom) {
        this.custom = custom;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getPage() {
        return page;
    }

    public Boolean isFacet() {
        return facet;
    }

    public Integer getFacetSize() {
        return facetSize;
    }

    public Boolean isScore() {
        return score;
    }

    public Map<String, String> getFq() {
        return fq;
    }

    public List<String> getFl() {
        return fl;
    }

    public Boolean isQueryInfo() {
        return queryInfo;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public Multimap<String, String> toMap() {
        Multimap<String, String> map = HashMultimap.create();

        if (limit != null && limit > 0) {
            map.put("limit", limit.toString());
        }

        if (page != null && page > 0) {
            map.put("page", page.toString());
        }

        if (facet != null && facetSize != null && facetSize > 0) {
            map.put("facet", String.valueOf(facet));
            map.put("facetsize", facetSize.toString());
        }

        if (score != null) {
            map.put("score", String.valueOf(score));
        }

        if (fq != null) {
            for (Map.Entry<String, String> entry : fq.entrySet()) {
                map.put("fq", entry.getKey() + ":" + entry.getValue());
            }
        }

        if (fl != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < fl.size(); i++) {
                builder.append(fl.get(i));
                if (i < fl.size() - 1) {
                    builder.append(',');
                }
            }
            map.put("fl", builder.toString());
        }

        if (queryInfo != null) {
            map.put("qinfo", String.valueOf(queryInfo));
        }

        if (custom != null) {
            map.putAll(Multimaps.forMap(custom));
        }

        return map;
    }
}
