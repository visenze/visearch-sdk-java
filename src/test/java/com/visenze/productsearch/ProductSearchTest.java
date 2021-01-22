package com.visenze.productsearch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.visenze.common.util.ViJsonAny;
import com.visenze.productsearch.param.ImageSearchParam;
import com.visenze.productsearch.param.VisualSimilarParam;
import com.visenze.productsearch.response.ProductInfo;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
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
    final String END_POINT = "https://search-dev.visenze.com//v1";
    final String APP_KEY = "";
    final Integer PLACEMENT_ID = 1;
    final String IMG_URL = "https://img.ltwebstatic.com/images2_pi/2019/09/09/15679978193855617200_thumbnail_900x1199.jpg";
    final String IMG_FILEPATH = "/Users/visenze/Downloads/photoshoot-outfits-ideas-white-t-shirt.jpg";

    /**
     * Retrieve a ProductSearch object with hardcoded keys for testing
     */
    public ProductSearch getProductSearch() {
        return new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
                .setApiEndPoint(END_POINT)
                .build();
    }

    @Test
    public void testByUrl() {
        ProductSearchResponse responseUrl = testImageSearchByUrl(IMG_URL);
    }

    @Test
    public void testByFile() {
        ProductSearchResponse responseFile = testImageSearchByFile(IMG_FILEPATH);
    }

    @Test
    public void testCasesWithDependencies() {
        ProductSearchResponse responseUrl = testImageSearchByUrl(IMG_URL);
        ProductSearchResponse responseFile = testImageSearchByFile(IMG_FILEPATH);
        ProductSearchResponse responseID1 = testImageSearchByID(responseUrl.getImageId());
        ProductSearchResponse responseID2 = testImageSearchByID(responseFile.getImageId());
    }

    /**
     * Calls the imageSearch using image url
     *
     * @param url The url path to the image
     *
     * @return Search response
     */
    public ProductSearchResponse testImageSearchByUrl(String url) {
        // configure your dummy data here:
        ImageSearchParam param = new ImageSearchParam(url, null);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        ViSearchHttpResponse res = sdk.imageSearch(param);
        return ProductSearchResponse.From(res);
    }

    /**
     * Calls the imageSearch using image file
     *
     * @param filepath The filepath to the image file
     *
     * @return Search response
     */
    public ProductSearchResponse testImageSearchByFile(String filepath) {
        // configure your dummy data here:
        ImageSearchParam param = new ImageSearchParam(new File(filepath));
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        ViSearchHttpResponse res = sdk.imageSearch(param);
        return ProductSearchResponse.From(res);
    }

    /**
     * Calls the imageSearch using image id (provided by a response)
     *
     * @param imageID ID of a referenced image
     *
     * @return Search response
     */
    public ProductSearchResponse testImageSearchByID(String imageID) {
        // configure your dummy data here:
        ImageSearchParam param = new ImageSearchParam(null, imageID);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        ViSearchHttpResponse res = sdk.imageSearch(param);
        return ProductSearchResponse.From(res);
    }


    /**
     * Calls the visualSimilarSearch using product id (provided by a response)
     *
     * @param product_id ID of a product from search results
     *
     * @return Search response
     */
    public ProductSearchResponse testVisualSimilarSearch(String product_id) {
        // configure your dummy data here:
        VisualSimilarParam param = new VisualSimilarParam(product_id);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        ViSearchHttpResponse res = sdk.visualSimilarSearch(param);
        return ProductSearchResponse.From(res);
    }
}