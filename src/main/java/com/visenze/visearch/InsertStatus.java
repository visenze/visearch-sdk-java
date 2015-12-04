package com.visenze.visearch;


import com.visenze.visearch.internal.ResponseBase;

import java.util.Date;
import java.util.List;

public class InsertStatus extends ResponseBase {

    private final String transId;
    private final Integer processedPercent;
    private final Integer total;
    private final Integer successCount;
    private final Integer failCount;
    private final Date startTime;
    private final Date updateTime;
    private final List<InsertError> errorList;
    private final Integer errorPage;
    private final Integer errorLimit;

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
