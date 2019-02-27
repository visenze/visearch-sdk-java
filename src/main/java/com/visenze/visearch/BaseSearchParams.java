package com.visenze.visearch;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.*;

/**
 * Common parameters for /search, /colorsearch, and /uploadsearch.
 * Provide setters to override default values.
 */
public class BaseSearchParams<P extends BaseSearchParams<P>> {

    private static final List<String> DEFAULT_FACET_FIELD = Lists.newArrayList();
    private static final Boolean DEFAULT_SCORE = false;
    private static final Map<String, String> DEFAULT_FQ = new HashMap<String, String>();
    private static final List<String> DEFAULT_FL = Lists.newArrayList();
    private static final Boolean DEFAULT_QINFO = false;
    private static final Map<String, String> DEFAULT_CUSTOM = new HashMap<String, String>();
    private static final Boolean DEFAULT_GET_ALL_FL = false;
    private static final Boolean DEFAULT_DEDUP = false;
    private static final Map<String, String> DEFAULT_VS_CONFIG = new HashMap<String, String>();



    protected Optional<Integer> page = Optional.absent();
    protected Optional<Integer> limit = Optional.absent();
    protected Optional<List<String>> facets = Optional.absent();
    protected Optional<Integer> facetsLimit = Optional.absent();
    protected Optional<Boolean> facetsShowCount = Optional.absent();
    protected Optional<Boolean> score = Optional.absent();
    protected Optional<Float> scoreMin = Optional.absent();
    protected Optional<Float> scoreMax = Optional.absent();
    protected Optional<Map<String, String>> fq = Optional.absent();
    protected Optional<Map<String, String>> vsfq = Optional.absent();
    protected Optional<List<String>> fl = Optional.absent();
    protected Optional<List<String>> vsfl = Optional.absent();
    protected Optional<Boolean> getAllFl = Optional.absent();
    protected Optional<Boolean> qInfo = Optional.absent();

    //VS-1693 support for vsconfig
    protected Optional<Map<String, String>> vsConfig = Optional.absent();

    protected Optional<Map<String, String>> custom = Optional.absent();

    protected Optional<Boolean> dedup = Optional.absent();
    protected Optional<Float> dedupThreshold = Optional.absent();

    protected Optional<String> groupBy = Optional.absent();
    protected Optional<Integer>  groupLimit = Optional.absent();

    protected Optional<String> sortBy = Optional.absent();
    protected Optional<String> sortGroupBy = Optional.absent();
    protected Optional<String> sortGroupStrategy = Optional.absent();


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
    public P setFacets(List<String> facetField) {
        this.facets = Optional.fromNullable(facetField);
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

    public void setVsConfig(Map<String, String> map) {
        this.vsConfig = Optional.fromNullable(map);
    }

    public Optional<Map<String, String>> getVsfq() {
        return vsfq;
    }

    public void setVsfq(Map<String, String> vsfq) {
        this.vsfq = Optional.fromNullable(vsfq);
    }

    public Optional<List<String>> getVsfl() {
        return vsfl;
    }

    public void setVsfl(List<String> vsfl) {
        this.vsfl = Optional.fromNullable(vsfl);
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

    @SuppressWarnings("unchecked")
    public P setFacetsLimit(Integer facetsLimit) {
        this.facetsLimit = Optional.fromNullable(facetsLimit);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setFacetsShowCount(Boolean facetsShowCount) {
        this.facetsShowCount = Optional.fromNullable(facetsShowCount);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setGroupBy(String groupBy) {
        this.groupBy = Optional.fromNullable(groupBy);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setGroupLimit(Integer groupLimit) {
        this.groupLimit = Optional.fromNullable(groupLimit);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setSortBy(String sortBy) {
        this.sortBy = Optional.fromNullable(sortBy);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setSortGroupBy(String sortGroupBy) {
        this.sortGroupBy = Optional.fromNullable(sortGroupBy);
        return (P) this;
    }

    @SuppressWarnings("unchecked")
    public P setSortGroupStrategy(String sortGroupStrategy) {
        this.sortGroupStrategy = Optional.fromNullable(sortGroupStrategy);
        return (P) this;
    }

    public Integer getPage() {
        return page.orNull();
    }

    public Integer getLimit() {
        return limit.orNull();
    }

    public List<String> getFacets() {
        return facets.or(DEFAULT_FACET_FIELD);
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

    public Integer getFacetsLimit() {
        return facetsLimit.orNull();
    }

    public Boolean getFacetsShowCount() {
        return facetsShowCount.orNull();
    }

    public String getGroupBy() { return groupBy.orNull(); }

    public Integer getGroupLimit() { return groupLimit.orNull(); }

    public String getSortBy() {
        return sortBy.orNull();
    }

    public String getSortGroupBy() {
        return sortGroupBy.orNull();
    }

    public String getSortGroupStrategy() {
        return sortGroupStrategy.orNull();
    }

    public Map<String, String> getVsConfig() {
        return vsConfig.or(DEFAULT_VS_CONFIG);
    }

    public Multimap<String, String> toMap() {
        Multimap<String, String> map = HashMultimap.create();

        if (getPage() != null) {
            map.put(PAGE, getPage().toString());
        }
        if (getLimit() != null) {
            map.put(LIMIT, getLimit().toString());
        }

        if (groupBy.isPresent()){
            map.put(GROUP_BY, getGroupBy() );
        }

        if (groupLimit.isPresent()){
            map.put(GROUP_LIMIT, getGroupLimit().toString() );
        }

        if (!getFacets().isEmpty()) {
            map.put(FACETS, Joiner.on(COMMA).join(getFacets()));
        }
        if (facetsLimit.isPresent()) {
            map.put(FACETS_LIMIT, facetsLimit.get().toString());
        }
        if (facetsShowCount.isPresent()) {
            map.put(FACETS_SHOW_COUNT, facetsShowCount.get().toString());
        }
        if (isScore()) {
            map.put(SCORE, TRUE);
        } else {
            map.put(SCORE, FALSE);
        }

        if (getScoreMin() != null) {
            map.put(SCORE_MIN, getScoreMin().toString());
        }
        if (getScoreMax() != null) {
            map.put(SCORE_MAX, getScoreMax().toString());
        }

        if (sortBy.isPresent()) {
            map.put(SORT_BY, getSortBy() );
        }

        if (sortGroupBy.isPresent()) {
            map.put(SORT_GROUP_BY, getSortGroupBy());
        }

        if (sortGroupStrategy.isPresent()) {
            map.put(SORT_GROUP_STRATEGY, getSortGroupStrategy());
        }

        for (Map.Entry<String, String> entry : getFq().entrySet()) {
            map.put(FQ, entry.getKey() + COLON + entry.getValue());
        }

        if (vsfq.isPresent()) {
            for (Map.Entry<String, String> entry : vsfq.get().entrySet()) {
                map.put(VS_FQ, entry.getKey() + COLON + entry.getValue());
            }
        }

        if (vsConfig.isPresent()) {
            for (Map.Entry<String, String> entry : vsConfig.get().entrySet()) {
                map.put(VS_CONFIG, entry.getKey() + COLON + entry.getValue());
            }
        }

        if (vsfl.isPresent()) {
            for (String filter : vsfl.get()) {
                map.put(VS_FL, filter);
            }
        }

        for (String filter : getFl()) {
            map.put(FL, filter);
        }

        if (isGetAllFl()) {
            map.put(GET_ALL_FL, TRUE);
        }

        if (isQInfo()) {
            map.put(QINFO, TRUE);
        }

        if (isDedup()) {
            map.put(DEDUP, TRUE);
        }

        if (getDedupThreshold() != null) {
            map.put(DEDUP_SCORE_THRESHOLD, getDedupThreshold().toString());
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
