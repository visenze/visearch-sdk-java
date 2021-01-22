package com.visenze.productsearch;

import com.visenze.productsearch.param.ImageSearchParam;
import com.visenze.productsearch.param.VisualSimilarParam;
import com.visenze.productsearch.response.ProductInfo;
import com.visenze.visearch.internal.InternalViSearchException;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;

/**
 * <h1> ProductSearch Test Cases </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 22 Jan 2021
 */
public class ProductSearchTest {
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

    /**
     * Calls the imageSearch using image url
     *
     * @param url The url path to the image
     *
     * @return Search response
     */
    public ProductSearchResponse imageSearchByUrl(String url) {
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
    public ProductSearchResponse imageSearchByFile(String filepath) {
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
    public ProductSearchResponse imageSearchByID(String imageID) {
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
    public ProductSearchResponse visualSimilarSearch(String product_id) {
        // configure your dummy data here:
        VisualSimilarParam param = new VisualSimilarParam(product_id);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        ViSearchHttpResponse res = sdk.visualSimilarSearch(param);
        return ProductSearchResponse.From(res);
    }

    @Test
    public void byUrl() {
        try {
            ProductSearchResponse responseUrl = imageSearchByUrl(IMG_URL);
        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void byFile() {
        try {
            ProductSearchResponse responseFile = imageSearchByFile(IMG_FILEPATH);
        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Once the product_id API is up and running - uncomment the @Test
     * annotation to turn on testing for this function
     */
    // @Test
    public void casesWithDependencies() {
        try {
            ProductSearchResponse responseUrl = imageSearchByUrl(IMG_URL);
            ProductSearchResponse responseFile = imageSearchByFile(IMG_FILEPATH);
            ProductSearchResponse responseID1 = imageSearchByID(responseUrl.getImageId());
            ProductSearchResponse responseID2 = imageSearchByID(responseFile.getImageId());

            List<ProductInfo> prods1 = responseID1.getResult();
            Random rand = new Random();
            int randomIndex = rand.nextInt(prods1.size());
            ProductSearchResponse responseSimilar1 = visualSimilarSearch(prods1.get(randomIndex).getProductId());
            assertEquals("OK", responseSimilar1.getStatus());

            List<ProductInfo> prods2 = responseID2.getResult();
            randomIndex = rand.nextInt(prods2.size());
            ProductSearchResponse responseSimilar2 = visualSimilarSearch(prods2.get(randomIndex).getProductId());
            assertEquals("OK", responseSimilar2.getStatus());

        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }
}