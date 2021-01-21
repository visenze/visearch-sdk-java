package com.visenze.productsearch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.visenze.common.util.ViJsonAny;
import com.visenze.productsearch.param.ImageSearchParam;
import com.visenze.productsearch.param.VisualSimilarParam;
import com.visenze.productsearch.response.ProductInfo;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * <h1> ProductSearch Test Cases </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 22 Jan 2021
 */
public class ProductSearchTest extends TestCase {

    /**
     * Retrieve a ProductSearch object with hardcoded keys for testing
     */
    public ProductSearch getProductSearch() {
        return new ProductSearch.Builder("",1)
                .setApiEndPoint("https://search-dev.visenze.com//v1")
                .build();
    }

    @Test
    public void testImageSearchByImageUrl() {
        final String dummy_image_url = "https://media.everlane.com/image/upload/c_fill,dpr_2.0,f_auto,g_face:center,q_auto,w_auto:100:1200/v1/i/83c49d92_ab01.jpg";

        ProductSearch sdk = getProductSearch();
        ImageSearchParam imageSearchParam = new ImageSearchParam(dummy_image_url, null);
        imageSearchParam.setScore(true);
        imageSearchParam.setReturnFieldsMapping(true);
        // perform search with dummy data
        ViSearchHttpResponse imageSearchResponse = sdk.imageSearch(imageSearchParam);
        ProductSearchResponse response = ProductSearchResponse.From(imageSearchResponse);

        // get all results in this 'page'
        for (ProductInfo p : response.getResult()) {
            // get all entries in it's 'data' field, test to see what type it is
            // map for 'currency', array for 'colors', etc
            for (Map.Entry<String, ViJsonAny> d : p.getData().entrySet()){
                String key = d.getKey();
                ViJsonAny val = d.getValue();
                if (val.isArray()) {
                    List<String> a = val.getAsList(new TypeReference<List<String>>() {});
                }
                else if (val.isValue()) {
                    String a = val.getAsValue(new TypeReference<String>() {});
                }
                else {
                    Map<String, String> currency_data = val.getAsMap(new TypeReference<Map<String, String>>() {});
                    // test sales_price field
                    if (key.equals("price")) {
                        assertNotNull(currency_data.get("currency"));
                        assertNotNull(currency_data.get("value"));
                    }
                }
            }
        }

    }

    public void testVisualSimilarSearch() {
        // VisualSimilarParam visualSimilarParam = new VisualSimilarParam();
        //ViHttpResponse visualSimilarResponse = sdk.visualSimilarSearch(visualSimilarParam);
        //ProductSearchResponse response = ProductSearchResponse.From(visualSimilarResponse);
    }
}