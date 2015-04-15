package com.visenze.visearch.example;


import com.visenze.visearch.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ViSearchExample {

    public static void main(String[] args) {
        ViSearch visearch = new ViSearch("your_api_key", "your_api_secret");

        SearchParams searchParam = new SearchParams("imName");
        Map<String, String> fq = new HashMap<String, String>();
        fq.put("field1", "value1");
        searchParam.setFq(fq);
        PagedSearchResult pagedSearchResult = visearch.search(searchParam);
        pagedSearchResult.getResult();

        ColorSearchParams colorSearchParameters =
                new ColorSearchParams("1b3c7e");
        PagedSearchResult colorResult = visearch.colorSearch(colorSearchParameters);
        colorResult.getResult();

        File imageFile = new File("/path/to/your/image");
        UploadSearchParams uploadSearchParams =
                new UploadSearchParams(imageFile);
        PagedSearchResult uploadResult = visearch.uploadSearch(uploadSearchParams, ResizeSettings.STANDARD);
        uploadResult.getResult();
    }

}
