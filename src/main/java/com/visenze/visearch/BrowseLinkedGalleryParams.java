package com.visenze.visearch;

import com.google.common.collect.Multimap;

/**
 * Created by Hung on 8/7/24.
 */
public class BrowseLinkedGalleryParams extends BaseSearchParams<BrowseLinkedGalleryParams> {
    private String strategyId;

    public BrowseLinkedGalleryParams(String strategyId) {
        this.strategyId = strategyId;
    }

    @Override
    public Multimap<String, String> toMap() {
        Multimap<String, String> map = super.toMap();

        if (strategyId != null) {
            map.put("strategy_id", strategyId.toString());
        }

        return map;
    }

    public String getStrategyId() {
        return strategyId;
    }
}
