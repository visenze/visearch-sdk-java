package com.visenze.visearch;

import java.util.List;

public class InsertTrans {

    private String transId;
    private Integer total;
    private List<InsertError> errorList;

    public InsertTrans(String transId, Integer total, List<InsertError> errorList) {
        this.transId = transId;
        this.total = total;
        this.errorList = errorList;
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
