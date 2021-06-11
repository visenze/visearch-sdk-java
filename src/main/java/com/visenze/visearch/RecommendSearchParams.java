package com.visenze.visearch;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.List;

/**
 * Created by david.huang on 2021-06-04
 */
public class RecommendSearchParams extends SearchParams {

    private static final List<String> DEFAULT_DEDUP_BY = Lists.newArrayList();

    private String recommendationAlgorithm;

    private Integer altLimit;

    protected Optional<List<String>> dedupBy = Optional.absent();

    public RecommendSearchParams(String imName) {
        super(imName);
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (recommendationAlgorithm != null) {
            map.put("recommendation_algorithm", recommendationAlgorithm);
        }

        int altLimitValue = 5;
        if (altLimit != null) {
            altLimitValue = altLimit.intValue();
        }
        map.put("alt_limit", String.valueOf(altLimitValue));

        if (dedupBy.isPresent()) {
            for (String value : dedupBy.get()) {
                map.put("dedup_by", value);
            }
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

    public List<String> getDedupBy() {
        return dedupBy.or(DEFAULT_DEDUP_BY);
    }

    public void setDedupBy(List<String> dedupBy) {
        this.dedupBy = Optional.fromNullable(dedupBy);
    }
}
