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

    private Boolean showPinnedImNames;
    private Boolean showExcludedImNames;

    private Integer setLimit;
    private Boolean useSetBasedCtl;

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

        if (showPinnedImNames != null) {
            map.put("show_pinned_im_names", showPinnedImNames.toString());
        }

        if (showExcludedImNames != null) {
            map.put("show_excluded_im_names", showExcludedImNames.toString());
        }

        if (useSetBasedCtl != null) {
            map.put("use_set_based_ctl", useSetBasedCtl.toString());
        }

        if (setLimit != null) {
            map.put("set_limit", setLimit.toString());
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

    public Boolean getShowPinnedImNames() {
        return showPinnedImNames;
    }

    public void setShowPinnedImNames(Boolean showPinnedImNames) {
        this.showPinnedImNames = showPinnedImNames;
    }

    public Boolean getShowExcludedImNames() {
        return showExcludedImNames;
    }

    public void setShowExcludedImNames(Boolean showExcludedImNames) {
        this.showExcludedImNames = showExcludedImNames;
    }

    public Integer getSetLimit() {
        return setLimit;
    }

    public void setSetLimit(Integer setLimit) {
        this.setLimit = setLimit;
    }

    public Boolean getUseSetBasedCtl() {
        return useSetBasedCtl;
    }

    public void setUseSetBasedCtl(Boolean useSetBasedCtl) {
        this.useSetBasedCtl = useSetBasedCtl;
    }
}
