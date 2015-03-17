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

    protected List<String> facetField;

    protected Boolean score;

    protected Float scoreMin;

    protected Float scoreMax;

    protected Map<String, String> fq;

    protected List<String> fl;

    protected Boolean queryInfo;

    protected Map<String, String> custom;

    public BaseSearchParams() {
        this.limit = 10;
        this.page = 1;
        this.facet = null;
        this.facetField = null;
        this.score = null;
        this.scoreMin = null;
        this.scoreMax = null;
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

    public BaseSearchParams setFacetField(List<String> facetField) {
        this.facetField = facetField;
        return this;
    }

    public BaseSearchParams setScore(Boolean score) {
        this.score = score;
        return this;
    }

    public BaseSearchParams setScoreMin(Float scoreMin) {
        this.scoreMin = scoreMin;
        return this;
    }

    public BaseSearchParams setScoreMax(Float scoreMax) {
        this.scoreMax = scoreMax;
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

    public List<String> getFacetField() {
        return facetField;
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

        if (facet != null && facetField != null && facetField.size() > 0) {
            map.put("facet", String.valueOf(facet));
            for (String facetFieldItem : facetField) {
                map.put("facet_field", facetFieldItem);
            }
        }

        if (score != null) {
            map.put("score", String.valueOf(score));
        }

        if (scoreMin != null) {
            map.put("score_min", String.valueOf(scoreMin));
        }

        if (scoreMax != null) {
            map.put("score_max", String.valueOf(scoreMax));
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
