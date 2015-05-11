package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.InsertError;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class InsertTransMixin {

    protected String transId;
    protected Integer total;
    protected List<InsertError> errorList;

    public InsertTransMixin(@JsonProperty("trans_id") String transId,
                            @JsonProperty("total") Integer total,
                            @JsonProperty("error") List<InsertError> errorList) {
        this.transId = transId;
        this.total = total;
        this.errorList = errorList;
    }

}
