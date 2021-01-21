package com.visenze.productsearch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.visenze.common.http.ViHttpResponse;
import com.visenze.common.util.ViJsonAny;
import com.visenze.productsearch.param.ImageSearchParam;
import com.visenze.productsearch.param.VisualSimilarParam;
import com.visenze.productsearch.response.ProductInfo;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ProductSearchTest extends TestCase {

    @Test
    public void testProductSearch() {
        ProductSearch sdk = new ProductSearch.Builder("",1)
                .setApiEndPoint("https://search-dev.visenze.com//v1")
                .build();
        testImageSearch(sdk);
        testVisualSimilarSearch(sdk);
    }

    public void testImageSearch(@NotNull ProductSearch sdk) {
        ImageSearchParam imageSearchParam = new ImageSearchParam("https://media.everlane.com/image/upload/c_fill,dpr_2.0,f_auto,g_face:center,q_auto,w_auto:100:1200/v1/i/83c49d92_ab01.jpg", null);
        imageSearchParam.setScore(true);
        imageSearchParam.setReturnFieldsMapping(true);
        ViHttpResponse imageSearchResponse = sdk.imageSearch(imageSearchParam);
        ProductSearchResponse response = ProductSearchResponse.From(imageSearchResponse);

        for (ProductInfo p : response.getResult()) {
            for (Map.Entry<String, ViJsonAny> d : p.getData().entrySet()){
                String key = d.getKey();
                ViJsonAny val = d.getValue();
                if (val.isArray()) {
                    List<String> a = val.getAsList(new TypeReference<List<String>>() {});
                    int i = 0;
                }
                else if (val.isValue()) {
                    String a = val.getAsValue(new TypeReference<String>() {});
                    int i = 0;
                }
                else {
                    Map<String, String> a = val.getAsMap(new TypeReference<Map<String, String>>() {});
                    int i = 0;
                }
            }
        }
    }

    public void testVisualSimilarSearch(ProductSearch sdk) {
        VisualSimilarParam visualSimilarParam = new VisualSimilarParam();
        //ViHttpResponse visualSimilarResponse = sdk.visualSimilarSearch(visualSimilarParam);
        //ProductSearchResponse response = ProductSearchResponse.From(visualSimilarResponse);
    }
}