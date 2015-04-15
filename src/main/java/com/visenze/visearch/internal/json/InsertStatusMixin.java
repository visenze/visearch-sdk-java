package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.InsertError;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class InsertStatusMixin {

    final private String transId;
    final private Integer processedPercent;
    final private Integer total;
    final private Integer successCount;
    final private Integer failCount;
    final private Date startTime;
    final private Date updateTime;
    final private List<InsertError> errorList;
    final private Integer errorPage;
    final private Integer errorLimit;

    public InsertStatusMixin(@JsonProperty("trans_id") String transId,
                             @JsonProperty("processed_percent") Integer processedPercent,
                             @JsonProperty("total") Integer total,
                             @JsonProperty("success_count") Integer successCount,
                             @JsonProperty("fail_count") Integer failCount,
                             @JsonProperty("start_time") Date startTime,
                             @JsonProperty("update_time") Date updateTime,
                             @JsonProperty("error_list") List<InsertError> errorList,
                             @JsonProperty("error_page") Integer errorPage,
                             @JsonProperty("error_limit") Integer errorLimit) {
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

}
