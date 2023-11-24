package com.visenze.productsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.common.util.ViJsonMapper;
import com.visenze.productsearch.response.ErrorMsg;

/**
 * Created by Hung on 24/11/23.
 */
public abstract class BaseProductSearchResponse extends ViJsonMapper {

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


}
