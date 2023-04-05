package com.visenze.productsearch.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.Box;
import com.visenze.visearch.Facet;

/**
 * <h1> Product Result related by Object </h1>
 * This class represents a detected Object and all the products found associated
 * with it.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 17 Mar 2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectProductResult {

    @JsonProperty("type")
    private String type;

    @JsonProperty("score")
    private Float score;

    @JsonProperty("box")
    private List<Integer> box;

    @JsonProperty("attributes")
    public Map<String, String> attributes;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("result")
    private List<Product> result;

    // API-9128 add new STI fields
    @JsonProperty("id")
    private String id;

    @JsonProperty("category")
    private String category;

    @JsonProperty("name")
    private String name;

    @JsonProperty("excluded_pids")
    private List<String> excludedPids;

    @JsonProperty("facets")
    private List<Facet> facets;

    public String getType() { return type; }

    public Float getScore() { return score; }

    public List<Integer> getBox() { return box; }

    public Map<String,String> getAttributes() { return attributes; }

    public Integer getTotal() { return total; }

    public List<Product> getResult() { return result; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getExcludedPids() {
        return excludedPids;
    }

    public void setExcludedPids(List<String> excludedPids) {
        this.excludedPids = excludedPids;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }
}
