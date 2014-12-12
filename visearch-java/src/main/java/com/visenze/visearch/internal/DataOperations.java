package com.visenze.visearch.internal;

import com.visenze.visearch.Image;
import com.visenze.visearch.InsertTransaction;
import com.visenze.visearch.PagedResult;

import java.util.List;

public interface DataOperations {

    InsertTransaction insert(List<Image> imageList);

    PagedResult<InsertTransaction> getStatus(Integer page, Integer limit);

    InsertTransaction getStatus(String transactionId);

    void remove(List<Image> imageList);

}
