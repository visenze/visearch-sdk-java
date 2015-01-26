package com.visenze.example;

import com.visenze.visearch.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ViSearchSDKExample {

    public static void main(String[] args) {
        ViSearch visearch = new ViSearch("your_access_key", "your_secret_key");

        SearchParams searchParam = new SearchParams("imName");
        Map<String, String> fq = new HashMap<String, String>();
        fq.put("field1", "value1");
        searchParam.setFq(fq);
        PagedSearchResult<ImageResult> pagedSearchResult = visearch.search(searchParam);
        pagedSearchResult.getResult();

        ColorSearchParams colorSearchParameters =
                new ColorSearchParams("1b3c7e");
        PagedSearchResult<ImageResult> colorResult = visearch.colorSearch(colorSearchParameters);
        colorResult.getResult();

        File imageFile = new File("/path/to/your/image");
        UploadSearchParams uploadSearchParams =
                new UploadSearchParams(imageFile);
        PagedSearchResult<ImageResult> uploadResult = visearch.uploadSearch(uploadSearchParams, ResizeSettings.STANDARD);
        uploadResult.getResult();
    }

}
