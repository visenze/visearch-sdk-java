package com.visenze.productsearch.param;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.*;
import static com.visenze.common.util.MultimapUtil.putIfPresent;

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
     * If set to true, API will return the query product's metadata
     */
    protected Optional<Boolean> returnProductInfo = Optional.absent();

    /**
     * For recommendation: The max number of alternatives following per recommendation results.
     */
    protected Optional<Integer> alt_limit = Optional.absent();

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

        Multimap<String, String> multimap = super.toMultimap();

        putIfPresent(multimap, returnProductInfo, RETURN_PRODUCT_INFO);
        putIfPresent(multimap, alt_limit, ALT_LIMIT);

        return multimap;
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

    /**
     * Get returnProductInfo
     *
     * @return returnProductInfo
     */
    public Boolean getReturnProductInfo() { return returnProductInfo.orNull(); }

    /**
     * Set returnProductInfo
     *
     * @param b If query should return product metadata
     */
    public void setReturnProductInfo(Boolean b) { this.returnProductInfo = Optional.fromNullable(b); }

    /**
     * Getter for alt_limit
     * @return
     */
    public Integer getAlt_limit() {
        return alt_limit.orNull();
    }

    /**
     * Setter for alt_limit
     * @param alt_limit
     */
    public void setAlt_limit(Integer alt_limit) {
        this.alt_limit = Optional.fromNullable(alt_limit);
    }
}
