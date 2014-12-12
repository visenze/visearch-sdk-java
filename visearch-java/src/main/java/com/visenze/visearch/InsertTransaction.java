package com.visenze.visearch;

import java.util.Date;

public class InsertTransaction {

    private String transactionId;

    private Integer total;

    private Integer failedCount;

    private Integer successCount;

    private Date startTime;

    private Date updateTime;

    public InsertTransaction(String transactionId, Integer total, Integer failedCount, Integer successCount,
                             Date startTime, Date updateTime) {
        this.transactionId = transactionId;
        this.total = total;
        this.failedCount = failedCount;
        this.successCount = successCount;
        this.startTime = startTime;
        this.updateTime = updateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

}
