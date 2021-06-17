package com.visenze.visearch;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.List;

/**
 * Created by david.huang on 2021-06-04
 */
public class RecommendSearchParams extends SearchParams {

    private String recommendationAlgorithm;

    private Integer altLimit;

    protected String dedupBy;

    public RecommendSearchParams(String imName) {
        super(imName);
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (recommendationAlgorithm != null) {
            map.put("recommendation_algorithm", recommendationAlgorithm);
        }

        if (altLimit != null) {
            map.put("alt_limit", altLimit.toString());
        }

        if (dedupBy != null) {
            map.put("dedup_by", dedupBy);
        }
        return map;
    }

    public String getRecommendationAlgorithm() {
        return recommendationAlgorithm;
    }

    public void setRecommendationAlgorithm(String recommendationAlgorithm) {
        this.recommendationAlgorithm = recommendationAlgorithm;
    }

    public Integer getAltLimit() {
        return altLimit;
    }

    public void setAltLimit(Integer altLimit) {
        this.altLimit = altLimit;
    }

    public String getDedupBy() {
        return dedupBy;
    }

    public void setDedupBy(String dedupBy) {
        this.dedupBy = dedupBy;
    }
}
