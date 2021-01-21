package com.visenze.productsearch.param;

import com.google.common.collect.Multimap;

/**
 *
 * <h1> Visually Similar Parameters </h1>
 * This class holds the specific parameters required to support Visually Similar
 * products search. It extends the BaseSearchParam to inherit all it's variables
 * that defines all general search parameter.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class VisualSimilarParam extends BaseProductSearchParam {
    /**
     * Product ID returned by other API. Will be used to append to visual
     * similar search path.
     */
    protected String product_id;

    /**
     * Constructor with the necessary parameters
     *
     * @param product_id product id to search against
     */
    public VisualSimilarParam(String product_id) {
        super();
        this.product_id = product_id;
    }

    /**
     * Convert this object into it's multimap representation.
     *
     * @return multimap of class variable to value
     */
    @Override
    public Multimap<String, String> toMultimap() {
        return super.toMultimap();
    }

    /**
     * Getter for product id
     *
     * @return product_id
     */
    public String getProductId() {
        return product_id;
    }

    /**
     * Setter for product id
     *
     * @param product_id This parameter is the product ID returned by the other
     *                  APIs
     */
    public void setProductId(String product_id) {
        this.product_id = product_id;
    }

}
