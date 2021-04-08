package com.visenze.productsearch.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public List<Product> getProducts() {
        return products;
    }
}
