package com.visenze.visearch;

import com.visenze.visearch.internal.ResponseBase;

import java.util.List;

public class PagedResult<T> extends ResponseBase {

    protected Integer page;

    protected Integer limit;

    protected Integer total;

    protected List<T> result;

    protected Integer groupLimit;

    public PagedResult() {}

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getTotal() {
        return total;
    }

    public List<T> getResult() {
        return result;
    }

    public Integer getGroupLimit() { return groupLimit; }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setGroupLimit(Integer groupLimit) { this.groupLimit = groupLimit; }
}
