package com.visenze.productsearch.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Strategy {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("algorithm")
    private String algorithm;

    @JsonProperty("fallback_algorithm")
    private String fallback_algorithm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getFallback_algorithm() {
        return fallback_algorithm;
    }

    public void setFallback_algorithm(String fallback_algorithm) {
        this.fallback_algorithm = fallback_algorithm;
    }
}
