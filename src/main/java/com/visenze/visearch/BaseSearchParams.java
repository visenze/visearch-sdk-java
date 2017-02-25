package com.visenze.visearch;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common parameters for /search, /colorsearch, and /uploadsearch.
 * Provide setters to override default values.
 */
public class BaseSearchParams<P extends BaseSearchParams<P>> {

    private static final Boolean DEFAULT_FACET = false;
    private static final List<String> DEFAULT_FACET_FIELD = Lists.newArrayList();
    private static final Boolean DEFAULT_SCORE = false;
    private static final Map<String, String> DEFAULT_FQ = new HashMap<String, String>();
    private static final List<String> DEFAULT_FL = Lists.newArrayList();
    private static final Boolean DEFAULT_QINFO = false;
    private static final Map<String, String> DEFAULT_CUSTOM = new HashMap<String, String>();
    private static final Boolean DEFAULT_GET_ALL_FL = false;
    private static final Boolean DEFAULT_DEDUP = false;

    protected Optional<Integer> page = Optional.absent();
    protected Optional<Integer> limit = Optional.absent();
    protected Optional<Boolean> facet = Optional.absent();
    protected Optional<List<String>> facetField = Optional.absent();
    protected Optional<Boolean> score = Optional.absent();
    protected Optional<Float> scoreMin = Optional.absent();
    protected Optional<Float> scoreMax = Optional.absent();
    protected Optional<Map<String, String>> fq = Optional.absent();
    protected Optional<List<String>> fl = Optional.absent();
    protected Optional<Boolean> getAllFl = Optional.absent();
    protected Optional<Boolean> qInfo = Optional.absent();
    protected Optional<Map<String, String>> custom = Optional.absent();

    protected Optional<Boolean> dedup = Optional.absent();
    protected Optional<Float> dedupThreshold = Optional.absent();

    @SuppressWarnings("unchecked")
    public P setPage(Integer page) {
        this.page = Optional.fromNullable(page);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setLimit(Integer limit) {
        this.limit = Optional.fromNullable(limit);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setFacet(Boolean facet) {
        this.facet = Optional.fromNullable(facet);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setFacetField(List<String> facetField) {
        this.facetField = Optional.fromNullable(facetField);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setScore(Boolean score) {
        this.score = Optional.fromNullable(score);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setScoreMin(Float scoreMin) {
        this.scoreMin = Optional.fromNullable(scoreMin);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setScoreMax(Float scoreMax) {
        this.scoreMax = Optional.fromNullable(scoreMax);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setFq(Map<String, String> fq) {
        this.fq = Optional.fromNullable(fq);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setFl(List<String> fl) {
        this.fl = Optional.fromNullable(fl);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setGetAllFl(Boolean getAllFl) {
        this.getAllFl = Optional.fromNullable(getAllFl);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setQInfo(Boolean qInfo) {
        this.qInfo = Optional.fromNullable(qInfo);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setCustom(Map<String, String> custom) {
        this.custom = Optional.fromNullable(custom);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setDedup(Boolean dedup) {
        this.dedup = Optional.fromNullable(dedup);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setDedupThreshold(Float dedupThreshold) {
        this.dedupThreshold = Optional.fromNullable(dedupThreshold);
        return (P) this;
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

    public Boolean isGetAllFl()
    {
        return getAllFl.or(DEFAULT_GET_ALL_FL);
    }

    public Boolean isQInfo() {
        return qInfo.or(DEFAULT_QINFO);
    }

    public Map<String, String> getCustom() {
        return custom.or(DEFAULT_CUSTOM);
    }

    public boolean isDedup() {
        return dedup.or(DEFAULT_DEDUP);
    }

    public Float getDedupThreshold() {
        return dedupThreshold.orNull();
    }

    public Multimap<String, String> toMap() {
        Multimap<String, String> map = HashMultimap.create();

        if (getPage() != null) {
            map.put("page", getPage().toString());
        }
        if (getLimit() != null) {
            map.put("limit", getLimit().toString());
        }

        if (isFacet() && !getFacetField().isEmpty()) {
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

        for (String filter : getFl()) {
            map.put("fl", filter);
        }

        if (isGetAllFl()) {
            map.put("get_all_fl", "true");
        }

        if (isQInfo()) {
            map.put("qinfo", "true");
        }

        if (isDedup()) {
            map.put("dedup", "true");
        }

        if (getDedupThreshold() != null) {
            map.put("dedup_score_threshold", getDedupThreshold().toString());
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
