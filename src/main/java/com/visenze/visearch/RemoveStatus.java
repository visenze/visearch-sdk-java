package com.visenze.visearch;

import com.visenze.visearch.internal.ResponseBase;

/**
 * Created by peng on 7/12/15.
 */
public class RemoveStatus extends ResponseBase {

    private int total;

    public RemoveStatus(int total) {
        this.total = total;
    }

    public RemoveStatus(String errorMessage, Throwable e, String rawResponse) {
        super.setErrorMessage(errorMessage);
        super.setCause(e);
        super.setRawResponseMessage(rawResponse);
    }

    public int getTotal() {
        return this.total;
    }
}
