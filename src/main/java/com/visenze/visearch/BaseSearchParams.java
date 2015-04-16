package com.visenze.visearch;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common parameters for /search, /colorsearch, and /uploadsearch.
 * Provide setters to override default values.
 */
public class BaseSearchParams {

    static final private Boolean DEFAULT_FACET = false;
    static final private List<String> DEFAULT_FACET_FIELD = Lists.newArrayList();
    static final private Boolean DEFAULT_SCORE = false;
    static final private Map<String, String> DEFAULT_FQ = new HashMap<String, String>();
    static final private List<String> DEFAULT_FL = Lists.newArrayList();
    static final private Boolean DEFAULT_QINFO = false;
    static final private Map<String, String> DEFAULT_CUSTOM = new HashMap<String, String>();

    protected Optional<Integer> page = Optional.absent();
    protected Optional<Integer> limit = Optional.absent();
    protected Optional<Boolean> facet = Optional.absent();
    protected Optional<List<String>> facetField = Optional.absent();
    protected Optional<Boolean> score = Optional.absent();
    protected Optional<Float> scoreMin = Optional.absent();
    protected Optional<Float> scoreMax = Optional.absent();
    protected Optional<Map<String, String>> fq = Optional.absent();
    protected Optional<List<String>> fl = Optional.absent();
    protected Optional<Boolean> qInfo = Optional.absent();
    protected Optional<Map<String, String>> custom = Optional.absent();


    public BaseSearchParams setPage(Integer page) {
        this.page = Optional.fromNullable(page);
        return this;
    }

    public BaseSearchParams setLimit(Integer limit) {
        this.limit = Optional.fromNullable(limit);
        return this;
    }

    public BaseSearchParams setFacet(Boolean facet) {
        this.facet = Optional.fromNullable(facet);
        return this;
    }

    public BaseSearchParams setFacetField(List<String> facetField) {
        this.facetField = Optional.fromNullable(facetField);
        return this;
    }

    public BaseSearchParams setScore(Boolean score) {
        this.score = Optional.fromNullable(score);
        return this;
    }

    public BaseSearchParams setScoreMin(Float scoreMin) {
        this.scoreMin = Optional.fromNullable(scoreMin);
        return this;
    }

    public BaseSearchParams setScoreMax(Float scoreMax) {
        this.scoreMax = Optional.fromNullable(scoreMax);
        return this;
    }

    public BaseSearchParams setFq(Map<String, String> fq) {
        this.fq = Optional.fromNullable(fq);
        return this;
    }

    public BaseSearchParams setFl(List<String> fl) {
        this.fl = Optional.fromNullable(fl);
        return this;
    }

    public BaseSearchParams setQInfo(Boolean qInfo) {
        this.qInfo = Optional.fromNullable(qInfo);
        return this;
    }

    public BaseSearchParams setCustom(Map<String, String> custom) {
        this.custom = Optional.fromNullable(custom);
        return this;
    }

    public Integer getPage() {
        return page.orNull();
    }

    public Integer getLimit() {
        return limit.orNull();
    }

    public boolean isFacet() {
        return facet.or(DEFAULT_FACET);
    }

    public List<String> getFacetField() {
        return facetField.or(DEFAULT_FACET_FIELD);
    }

    public boolean isScore() {
        return score.or(DEFAULT_SCORE);
    }

    public Float getScoreMin() {
        return scoreMin.orNull();
    }

    public Float getScoreMax() {
        return scoreMax.orNull();
    }

    public Map<String, String> getFq() {
        return fq.or(DEFAULT_FQ);
    }

    public List<String> getFl() {
        return fl.or(DEFAULT_FL);
    }

    public Boolean isQInfo() {
        return qInfo.or(DEFAULT_QINFO);
    }

    public Map<String, String> getCustom() {
        return custom.or(DEFAULT_CUSTOM);
    }

    public Multimap<String, String> toMap() {
        Multimap<String, String> map = HashMultimap.create();

        if (getPage() != null) {
            map.put("page", getPage().toString());
        }
        if (getLimit() != null) {
            map.put("limit", getLimit().toString());
        }

        if (isFacet() && getFacetField().size() > 0) {
            map.put("facet", "true");
            for (String facetFieldItem : getFacetField()) {
                map.put("facet_field", facetFieldItem);
            }
        }

        if (isScore()) {
            map.put("score", "true");
        }

        if (getScoreMin() != null) {
            map.put("score_min", getScoreMin().toString());
        }
        if (getScoreMax() != null) {
            map.put("score_max", getScoreMax().toString());
        }

        for (Map.Entry<String, String> entry : getFq().entrySet()) {
            map.put("fq", entry.getKey() + ":" + entry.getValue());
        }

        for (String fl : getFl()) {
            map.put("fl", fl);
        }

        if (isQInfo()) {
            map.put("qinfo", "true");
        }

        for (Map.Entry<String, String> entry : getCustom().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Preconditions.checkNotNull(key, "Custom search param key must not be null.");
            Preconditions.checkNotNull(value, "Custom search param value must not be null.");
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }
}
