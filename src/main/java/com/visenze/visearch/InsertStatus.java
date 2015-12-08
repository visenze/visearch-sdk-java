package com.visenze.visearch;


import com.visenze.visearch.internal.ResponseBase;

import java.util.Date;
import java.util.List;

public class InsertStatus extends ResponseBase {

    private String transId;
    private Integer processedPercent;
    private Integer total;
    private Integer successCount;
    private Integer failCount;
    private Date startTime;
    private Date updateTime;
    private List<InsertError> errorList;
    private Integer errorPage;
    private Integer errorLimit;

    public InsertStatus(String transId, Integer processedPercent,
                        Integer total, Integer successCount, Integer failCount,
                        Date startTime, Date updateTime,
                        List<InsertError> errorList, Integer errorPage, Integer errorLimit) {
        this.transId = transId;
        this.processedPercent = processedPercent;
        this.total = total;
        this.successCount = successCount;
        this.failCount = failCount;
        this.startTime = startTime;
        this.updateTime = updateTime;
        this.errorList = errorList;
        this.errorPage = errorPage;
        this.errorLimit = errorLimit;
    }

    public InsertStatus(String errorMessage, Throwable e, String rawResponse) {
        super.setErrorMessage(errorMessage);
        super.setCause(e);
        super.setRawResponseMessage(rawResponse);
    }

    public String getTransId() {
        return transId;
    }

    public Integer getProcessedPercent() {
        return processedPercent;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public List<InsertError> getErrorList() {
        return errorList;
    }

    public Integer getErrorPage() {
        return errorPage;
    }

    public Integer getErrorLimit() {
        return errorLimit;
    }

}
