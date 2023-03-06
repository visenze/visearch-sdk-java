package com.visenze.visearch;

import com.google.common.collect.Multimap;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.SHOW_BEST_PRODUCT_IMAGES;

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
    private Boolean showBestProductImages;

    public RecommendSearchParams(String imName) {
        super(imName);
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();

        putIfNotNull(map, "algorithm", algorithm);
        putIfNotNull(map, "alt_limit", altLimit);
        putIfNotNull(map, "dedup_by", dedupBy);
        putIfNotNull(map, "strategy_id", strategyId);
        putIfNotNull(map, "user_context", userContext);

        putIfNotNull(map, "show_pinned_im_names", showPinnedImNames);
        putIfNotNull(map, "show_excluded_im_names", showExcludedImNames);
        putIfNotNull(map, "use_set_based_ctl", useSetBasedCtl);
        putIfNotNull(map, "set_limit", setLimit);
        putIfNotNull(map, SHOW_BEST_PRODUCT_IMAGES, showBestProductImages );

        return map;
    }

    private void putIfNotNull(Multimap<String, String> map, String paramName, Object paramValue) {
        if (paramValue != null) {
            map.put(paramName, paramValue.toString());
        }
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

    public Boolean getShowBestProductImages() {
        return showBestProductImages;
    }

    public void setShowBestProductImages(Boolean showBestProductImages) {
        this.showBestProductImages = showBestProductImages;
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
