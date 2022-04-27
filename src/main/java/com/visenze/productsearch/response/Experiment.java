package com.visenze.productsearch.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Hung on 27/4/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Experiment {

    @JsonProperty("experiment_id")
    private int experimentId;

    @JsonProperty("variant_id")
    private int variantId;

    @JsonProperty("variant_name")
    private String variantName;

    @JsonProperty("strategy_id")
    private Integer strategyId;

    @JsonProperty("experiment_no_recommendation")
    private boolean expNoRecommendation;

    @JsonProperty("debug")
    private JsonNode debug;

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public boolean isExpNoRecommendation() {
        return expNoRecommendation;
    }

    public void setExpNoRecommendation(boolean expNoRecommendation) {
        this.expNoRecommendation = expNoRecommendation;
    }

    public JsonNode getDebug() {
        return debug;
    }

    public void setDebug(JsonNode debug) {
        this.debug = debug;
    }
}
