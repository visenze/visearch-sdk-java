package com.visenze.visearch;

import com.google.common.collect.Multimap;

/**
 * Created by david.huang on 2021-06-04
 */
public class RecommendSearchParams extends SearchParams {

    private String algorithm;

    private Integer altLimit;

    private String dedupBy;

    private String strategyId;

    private String userContext;

    public RecommendSearchParams(String imName) {
        super(imName);
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();
        if (algorithm != null) {
            map.put("algorithm", algorithm);
        }

        if (altLimit != null) {
            map.put("alt_limit", altLimit.toString());
        }

        if (dedupBy != null) {
            map.put("dedup_by", dedupBy);
        }

        if (strategyId != null) {
            map.put("strategy_id", strategyId);
        }

        if (userContext != null) {
            map.put("user_context", userContext);
        }

        return map;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
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

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getUserContext() {
        return userContext;
    }

    public void setUserContext(String userContext) {
        this.userContext = userContext;
    }
}
