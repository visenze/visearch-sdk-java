package com.visenze.productsearch;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.visenze.common.util.ViJsonAny;
import com.visenze.common.util.ViJsonMapper;
import com.visenze.productsearch.response.ErrorMsg;
import com.visenze.productsearch.response.GroupProductResult;
import com.visenze.productsearch.response.ObjectProductResult;
import com.visenze.productsearch.response.Product;
import com.visenze.productsearch.response.Strategy;
import com.visenze.visearch.ProductType;
import com.visenze.visearch.Facet;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.BOX;
import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.SCORE;
import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.TYPE;

/**
 * <h1> ProductSearchResponse </h1>
 * This class acts as a parser to help determine and break down the response
 * generated by a HTTP request method stored inside ViHttpResponse.
 *
 * The @JsonIgnoreProperties(ignoreUnknown=true) annotation tells JSON that this
 * class will ignore deserialization of fields found inside a json object that
 * does not exists within our class (by default error is thrown as we are
 * required to contain every field needed by the json object).
 *
 * <p>
 * This class aims to be Json compatible by implementing Jackson annotation
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 12 Jan 2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchResponse extends ViJsonMapper {
    /**
     * Each request is associated with an id to help with tracking api calls.
     *
     * The actual field name is called 'reqid' but we want to store it as
     * 'requestId' inside this class. The @JsonProperty("...") annotation tells
     * JSON that this variable 'requestId' will be serialized/deserialized as
     * 'reqid' from JSON's point of view.
     *      i.e. some_json_map_thing["reqid"] = this_class.requestId;
     */
    @JsonProperty("reqid")
    private String requestId;

    /**
     * The request status, either “OK”, or “fail”.
     */
    @JsonProperty("status")
    private String status;

    /**
     * Image ID. Can be used to search again without re-uploading.
     */
    @JsonProperty("im_id")
    private String imageId;

    /**
     * The result page number. Each response is tied to 1 'page' of a response.
     * Since there can be potentially 1000 results and we limit to 10 results at
     * a time, meaning 100 pages of 10 results each. This page number indicates
     * which page it is displaying.
     */
    @JsonProperty("page")
    private int page;

    /**
     * The number of results per page. Use to determine how many results we will
     * display one each 'page' at a time.
     */
    @JsonProperty("limit")
    private int limit;

    /**
     * Total number of search results.
     */
    @JsonProperty("total")
    private int total;

    /**
     * Error message and code if the request was not successful
     *      i.e. when status is “fail”.
     */
    @JsonProperty("error")
    private ErrorMsg error;

    /**
     * This list of product types picked up from the image.
     *
     * @see ProductType
     */
    @JsonProperty("product_types")
    private List<ProductType> productTypeList;

    /**
     * The list of product that are found based on searching parameter (visual
     * similar, image search). This list of products is not all the product
     * result but only results from a certain page. As there can be an arbitrary
     * 1000 results, if the request was sent with the 'limit' variable in params
     * and also received in 'limit' above (The number of results returned per
     * page), then we will have a total of, 1000 / limit, number of pages with
     * each page holding up to N=limit number of products (which would be the
     * size of this list). Which page this result belongs to is determined by
     * the 'page' variable sent in params and also received in 'page' variable
     * above.
     */
    @JsonProperty("result")
    private List<Product> result;


    /**
     * A map of Key-to-Value pairs. The keys in  it represent ViSenze's keys
     * (our field names) and the values associated with it is the Client's keys
     * (their field names). Use this to look-up ProductInfo's 'data' variable.
     *
     * @see Product
     */
    @JsonProperty("catalog_fields_mapping")
    private Map<String, String> catalogFieldsMapping;

    /**
     * List of facets from filtering
     */
    @JsonProperty("facets")
    private List<Facet> facets;

    @JsonProperty("product_info")
    private Product product;

    @JsonProperty("objects")
    private List<ObjectProductResult> objects;

    @JsonProperty("group_results")
    private List<GroupProductResult> groupProductResults;

    @JsonProperty("group_by_key")
    private String groupByKey;

    @JsonProperty("query_sys_meta")
    private Map<String, String> querySysMeta;

    @JsonProperty("explanation")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, ViJsonAny> explanation;

    @JsonProperty("strategy")
    private Strategy strategy;

    @JsonProperty("alt_limit")
    private Integer altLimit;

    /**
     * Delegated construction with a ViHttpResponse will automatically parse the
     * response into data members.
     *
     * Q: Why do we need to use a static function to create object instead of
     * calling constructor straight?
     * A: Because Java don't support self assignment. i.e. We cannot convert a
     * response json text body into T object from T's own constructor.
     *
     * @param response The ViHttpResponse received by calling ViHttpClient
     *                 api functions.
     * @return Product Search response
     */
    public static ProductSearchResponse fromResponse(ViSearchHttpResponse response) {
        try {
            return mapper.readValue(response.getBody(), ProductSearchResponse.class);
        } catch(IOException e){
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e.getMessage());
        }
    }

    /**
     * Each request is associated with an id to help with tracking api calls.
     *
     * @return String representation of the request identifier
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * The request status, either “OK”, or “fail”.
     *
     * @return String status of the request sent
     */
    public String getStatus() {
        return status;
    }

    /**
     * Image ID. Can be used to search again without re-uploading.
     *
     * @return The image id
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * Get Page number.
     *
     * @return The result page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Get Results per page.
     *
     * @return The number of results per page
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Get Number of results.
     *
     * @return The total number of results
     */
    public int getTotal() {
        return total;
    }

    /**
     * Get the error code.
     *
     * @return Map of error key-value pairs.
     */
    public ErrorMsg getError() {
        return error;
    }

    /**
     * Get the product types.
     *
     * @return A list of product type
     */
    public List<ProductType> getProductTypes() {
        return productTypeList;
    }

    /**
     * Get the mapping of ViSenze to Client fields.
     *
     * @return The, field name-to-field name, mapping
     */
    public Map<String, String> getCatalogFieldsMapping() {
        return catalogFieldsMapping;
    }

    /**
     * Get the list of product information for this page.
     *
     * @return result
     * @see Product
     */
    public List<Product> getResult() { return result; }

    public List<GroupProductResult> getGroupProductResults() {
        return groupProductResults;
    }

    public void setGroupProductResults(List<GroupProductResult> groupProductResults) {
        this.groupProductResults = groupProductResults;
    }

    /**
     * Get Facets if any was designated in the parameter when requesting API
     *
     * @return List of Facets used
     * @see Facet
     */
    public List<Facet> getFacets() { return facets; }

    /**
     * Get Products detected in Object grouping/category
     *
     * @return List of Products categorized by "Object" type
     * @see ObjectProductResult
     */
    public List<ObjectProductResult> getObjects() { return objects; }

    /**
     * Get Key that was used when using group_by functionality
     *
     * @return Grouping key
     */
    public String getGroupByKey() { return groupByKey; }

    /**
     * Get System query metadata
     *
     * @return Metadata
     */
    public Map<String, String> getQuerySysMeta() { return querySysMeta; }

    public Product getProduct() {
        return product;
    }

    /**
     * Reserved property. Explain the recommendation reason on the query level.
     * @return Explanation
     */
    public Map<String, ViJsonAny> getExplanation() {
        return explanation;
    }

    /**
     * The recommendation strategy applied.
     * @return Strategy information used in this query
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * The max number of alternatives following per recommendation results,
     * @return the value of alt_limit
     */
    public Integer getAltLimit() {
        return altLimit;
    }

    /**
     * Json requires JsonCreator or default ctors, but since we clash with
     * ViSearch's ProductTypes deserialization methods, we custom ours here.
     * @param node Json node
     *
     */
    @JsonSetter("product_types")
    public void setProductTypes(JsonNode node) {
        try {
            if (node.isArray()) {
                ArrayList<ProductType> list = new ArrayList<ProductType>();
                for (JsonNode n : node) {
                    ProductType productType = parseProductType(n);
                    list.add(productType);
                }

                productTypeList = list;
            }
        } catch (IllegalArgumentException e) {
            throw new InternalViSearchException(e.getMessage());
        }
    }

    private ProductType parseProductType(JsonNode n) {
        List<Integer> boxVals = null;
        String type = null;
        Float score = null;

        if (n.has(BOX)) {
            boxVals = mapper.convertValue(n.get(BOX), new TypeReference<List<Integer>>() {});
        }

        if (n.has(TYPE))
            type = n.get(TYPE).asText();

        if (n.has(SCORE))
            score = (float)n.get(SCORE).asDouble();

        return new ProductType(type, score, boxVals);
    }
}
