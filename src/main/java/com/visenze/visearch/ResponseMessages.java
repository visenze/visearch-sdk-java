package com.visenze.visearch;

/**
 * Created by peng on 3/12/15.
 */
public enum ResponseMessages {

    // for all requests
    SUCESS(0, "Success."),
    INVALID_KEY(11, "Invalid access key or secret key."),
    UNAUTHORIZED(12, "Unauthorized."),
    INVALID_IMAGE_SIZE(21, "Exceeds image collection size limit."),
    SYSTEM_BUSY(31, "System busy, please try again later."),
    SYSTEM_ERROR(32, "A system error is reported and we are fixing it right now."),
    NETWORK_ERROR(41, "A network error occurred when reading response from the ViSearch endpoint. " +
            "Please check your network connectivity and try again."),
    INVALID_ENDPOINT(42, "There was an error parsing the ViSearch endpoint. " +
            "Please ensure that your provided ViSearch endpoint is a well-formed URL and try again."),
    PARSE_RESPONSE_ERROR(43, "Could not parse the ViSearch response."),
    INVALID_RESPONSE_FORMAT(44, "ViSearch response have wrong contents."),

    // for data APIs
    TRANSACTION_NOT_FOUND(101, "Transaction not found with trans_id."),
    DATA_IMAGE_NOT_FOUND(102, "Image not found with im_name."),
    EXCEED_INSERT_BATCH_SIZE(103, "Exceeded maximum insert batch size of 100."),
    NO_IMAGE_INSERTED(104, "No image inserted."),
    EXCEED_IMAGE_PARA_NUM(105, "Number of parameters for image exceeds maximum limit of 32."),
    EXCEED_IMAGE_PARA_LENGTH(106, "{parameter} exceeds length limit."),
    INVALID_IMAGE_NAME(107, "Invalid im_name."),
    MISSING_IMAGE_URL(108, "Missing im_url."),
    INVALID_FIELD_TYPE(109, "{field} should be of data type {type}."),
    IMAGE_DOWNLOAD_ERROR(201, "Could not download the image from im_url."),
    DATA_INVALID_IMAGE_FORMAT(202, "Unsupported image format."),
    DATA_INVALID_IMAGE_DIMENSION(203, "Unsupported image dimension."),

    // for search APIs
    QUERY_PROCESSING(1001, "The query image is still being processed, please try again later."),
    SEARCH_IMAGE_NOT_FOUND(1101, "Image not found with im_name."),
    SEARCH_INVALID_IMAGE_FORMAT(1102, "Invalid image file format."),
    SEARCH_INVALID_IMAGE_FORMAT_OR_DIMENSION(1103, "The image format or dimension is invalid."),
    INVALID_IMAGE_OR_URL(1104, "Invalid image or im_url."),
    DOWNLOAD_IMAGE_ERROR(1105, "Could not download the image, please try again later."),
    DOWNLOAD_IMAGE_TIMEOUT(1106, "Timeout while downloading image, please try again later."),
    NO_IMAGE_INDEXED(1107, "App doesn't have any images."),
    INVALID_IMAGE_SOURCE(1108, "Must provide either an image File, InputStream of the image, " +
            "or a valid image url to perform upload search"),
    PARSE_EXCEPTION(1109, "Invalid/Bad/Unsupported format for parsing."),
    MISSING_IMAGE_ID(1110,"Missing im_id." ),
    MISSING_IMAGE_FILE(1110,"Missing im_file." ),
    MISSING_IMAGE_URL_OR_IMAGE_ID(1111,"Missing im_url or im_id." )
    ;

    private int code;
    private String message;

    private ResponseMessages(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
