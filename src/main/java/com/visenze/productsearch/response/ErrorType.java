package com.visenze.productsearch.response;

/**
 * <h1> Error in Responses </h1>
 * When a response is received via the ProductSearch APIs, they might contain
 * error details in the response's text body. Since the response text will
 * contain more than just error information, we will delegate the error extract
 * from the json string into this class.
 *
 *      i.e. "error":{ "code":"...", "message":"...", ...}
 *
 * This implies that the type of error is fixed and if the response error
 * changes, this class would also need to change accordingly.
 * <p>
 * This class aims to be Json compatible by implementing Jackson annotation
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 12 Jan 2021
 */
public class ErrorType {

    /**
     * Http status code, 200, 400, 404 etc.
     */
    private String code;

    /**
     * Custom message reply from the API (readable debugging info).
     */
    private String message;

    /**
     * Get the http code.
     *
     * @return Http status code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the message provided by the server/API.
     *
     * @return Custom message by backend
     */
    public String getMessage() {
        return message;
    }
}
