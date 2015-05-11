package com.visenze.visearch.internal.json;


import com.fasterxml.jackson.annotation.JsonProperty;

abstract class InsertErrorMixin {

    final private String imName;
    final private Integer index;
    final private Integer errorCode;
    final private String errorMessage;

    public InsertErrorMixin(@JsonProperty("im_name") String imName,
                            @JsonProperty("index") Integer index,
                            @JsonProperty("error_code") Integer errorCode,
                            @JsonProperty("error_message") String errorMessage) {
        this.imName = imName;
        this.index = index;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
