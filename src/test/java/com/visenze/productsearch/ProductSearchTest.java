package com.visenze.productsearch;

import com.visenze.productsearch.param.SearchByImageParam;
import com.visenze.productsearch.param.SearchByIdParam;
import com.visenze.productsearch.response.Product;
import com.visenze.visearch.internal.InternalViSearchException;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

/**
 * <h1> ProductSearch Test Cases </h1>
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 22 Jan 2021
 */
public class ProductSearchTest {
    final String END_POINT = "https://search-dev.visenze.com/v1";
    final String APP_KEY = "";
    final Integer PLACEMENT_ID = 1000;
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
        SearchByImageParam param = SearchByImageParam.newFromImageUrl(url);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        return sdk.imageSearch(param);
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
        SearchByImageParam param = SearchByImageParam.newFromImageFile(new File(filepath));
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        return sdk.imageSearch(param);
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
        SearchByImageParam param = SearchByImageParam.newFromImageId(imageID);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        return sdk.imageSearch(param);
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
        SearchByIdParam param = new SearchByIdParam(product_id);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch();
        return sdk.visualSimilarSearch(param);
    }

    @Test
    public void byUrl() {
        ProductSearchResponse responseUrl;
        try {
            responseUrl = imageSearchByUrl(IMG_URL);
            imageSearchByID(responseUrl.getImageId());
        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void byFile() {
        ProductSearchResponse responseFile;
        try {
            responseFile = imageSearchByFile(IMG_FILEPATH);
            imageSearchByID(responseFile.getImageId());
        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void byCatalogueFieldMappings() {
        ProductSearchResponse response;
        try {
            // get an initial response
            response = imageSearchByUrl(IMG_URL);
            // retrieve catalogue fields mapping from response
            Map<String, String> fieldsMap = response.getCatalogFieldsMapping();
            // create new request using response id (re-use image from initial
            // response)
            SearchByImageParam param = SearchByImageParam.newFromImageId(response.getImageId());
            param.setShowScore(true);
            param.setReturnFieldsMapping(true);
            // create list of attributes-to-get using fields mapping to get
            // client's fields name from ViSenze field name
            List<String> attrsToGet = new ArrayList<String>();
            attrsToGet.add(fieldsMap.get("product_url"));
            attrsToGet.add(fieldsMap.get("title"));
            attrsToGet.add(fieldsMap.get("category"));
            attrsToGet.add(fieldsMap.get("brand"));
            param.setAttrsToGet(attrsToGet);
            // create map of text filters using fields mapping to get client's
            // fileds name from ViSenze field name
            Map<String,String> textFilters = new HashMap<String, String>();
            textFilters.put(fieldsMap.get("category"), "top");
            param.setTextFilters(textFilters);
            // create map of filters using fields mapping to get client's fileds
            // name from ViSenze field name
            Map<String,String> filters = new HashMap<String, String>();
            filters.put(fieldsMap.get("brand"), "Pomelo");
            param.setFilters(filters);
            // execute search and return response
            ProductSearch sdk = getProductSearch();
            sdk.imageSearch(param);
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

            List<Product> prods1 = responseID1.getResult();
            Random rand = new Random();
            int randomIndex = rand.nextInt(prods1.size());
            ProductSearchResponse responseSimilar1 = visualSimilarSearch(prods1.get(randomIndex).getProductId());
            assertEquals("OK", responseSimilar1.getStatus());

            List<Product> prods2 = responseID2.getResult();
            randomIndex = rand.nextInt(prods2.size());
            ProductSearchResponse responseSimilar2 = visualSimilarSearch(prods2.get(randomIndex).getProductId());
            assertEquals("OK", responseSimilar2.getStatus());

        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }
}