package com.visenze.productsearch.param;

import com.google.common.collect.Multimap;

/**
 *
 * <h1> Search by ID Parameters </h1>
 * This class holds the specific parameters required to support searching by
 * product ID. It extends the BaseSearchParam to inherit all it's variables that
 * defines all general search parameter.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class SearchByIdParam extends BaseProductSearchParam {
    /**
     * Product ID returned by other API. Will be used to append to visual
     * similar search path.
     */
    protected String productId;

    /**
     * Constructor with the necessary parameters
     *
     * @param product_id product id to search against
     */
    public SearchByIdParam(String product_id) {
        super();
        this.productId = product_id;
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
     * @return productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Setter for product id
     *
     * @param productId This parameter is the product ID returned by the other
     *                  APIs
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

}
