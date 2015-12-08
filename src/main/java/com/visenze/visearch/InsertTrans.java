package com.visenze.visearch;

import com.visenze.visearch.internal.ResponseBase;

import java.util.List;

public class InsertTrans extends ResponseBase {

    private String transId;
    private Integer total;
    private List<InsertError> errorList;

    public InsertTrans(String transId, Integer total, List<InsertError> errorList) {
        this.transId = transId;
        this.total = total;
        this.errorList = errorList;
    }

    public InsertTrans(String errorMessage, Throwable e, String rawResponseMessage) {
        super.setErrorMessage(errorMessage);
        super.setCause(e);
        super.setRawResponseMessage(rawResponseMessage);
    }

    public String getTransId() {
        return transId;
    }

    public Integer getTotal() {
        return total;
    }

    public List<InsertError> getErrorList() {
        return errorList;
    }
}
