package com.visenze.productsearch.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Hung on 28/1/21.
 */
public class GroupProductResult {

    @JsonProperty("group_by_value")
    private String groupByValue;

    @JsonProperty("result")
    private List<Product> products;

    public String getGroupByValue() {
        return groupByValue;
    }

    public void setGroupByValue(String groupByValue) {
        this.groupByValue = groupByValue;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
