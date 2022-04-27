package com.visenze.productsearch.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.Box;

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

    public String getType() { return type; }

    public Float getScore() { return score; }

    public List<Integer> getBox() { return box; }

    public Map<String,String> getAttributes() { return attributes; }

    public Integer getTotal() { return total; }

    public List<Product> getResult() { return result; }
}
