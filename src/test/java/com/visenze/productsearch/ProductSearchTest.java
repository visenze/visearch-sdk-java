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
    final String END_POINT = "https://search-dev.visenze.com";
    final String SBI_KEY = "";
    final String VSR_KEY = "";
    final Integer SBI_PLACEMENT = 1000;
    final Integer VSR_PLACEMENT = 1002;
    final String IMG_URL = "https://img.ltwebstatic.com/images2_pi/2019/09/09/15679978193855617200_thumbnail_900x1199.jpg";
    final String IMG_FILEPATH = "";

    /**
     * Retrieve a ProductSearch object with hardcoded keys for testing
     */
    public ProductSearch getProductSearch(String key, Integer placement) {
        return new ProductSearch.Builder(key, placement)
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
        ProductSearch sdk = getProductSearch(SBI_KEY, SBI_PLACEMENT);
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
        ProductSearch sdk = getProductSearch(SBI_KEY, SBI_PLACEMENT);
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
        ProductSearch sdk = getProductSearch(SBI_KEY, SBI_PLACEMENT);
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
        ProductSearch sdk = getProductSearch(VSR_KEY, VSR_PLACEMENT);
        return sdk.visualSimilarSearch(param);
    }

    /**
     * Calls the recommendation using product id (provided by a response)
     *
     * @param product_id ID of a product from search results
     *
     * @return Search response
     */
    public ProductSearchResponse recommendation(String product_id) {
        // configure your dummy data here:
        SearchByIdParam param = new SearchByIdParam(product_id);
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);

        // execute search and return response
        ProductSearch sdk = getProductSearch(VSR_KEY, VSR_PLACEMENT);
        return sdk.recomendation(param);
    }

    @Test
    public void byUrl() {
        // cannot test without testing key
        if (VSR_KEY.isEmpty() || SBI_KEY.isEmpty())
            return;
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
        // cannot test without testing key
        if (VSR_KEY.isEmpty() || SBI_KEY.isEmpty())
            return;
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
        // cannot test without testing key
        if (VSR_KEY.isEmpty() || SBI_KEY.isEmpty())
            return;
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
            ProductSearch sdk = getProductSearch(SBI_KEY, SBI_PLACEMENT);
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
        // cannot test without testing key
        if (VSR_KEY.isEmpty() || SBI_KEY.isEmpty())
            return;
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

    // can see field mappings for certain valid facets
    @Test
    public void withFacets() {
        if (VSR_KEY.isEmpty() || SBI_KEY.isEmpty())
            return;
        try {
            SearchByImageParam param = SearchByImageParam.newFromImageUrl(IMG_URL);
            param.setFacetsShowCount(true);
            param.setFacetsLimit(10);
            param.setFacets(Arrays.asList("brand_name", "merchant_category"));

            ProductSearch sdk = getProductSearch(SBI_KEY, SBI_PLACEMENT);
            ProductSearchResponse response = sdk.imageSearch(param);

            assertEquals(response.getFacets().isEmpty(), false);

        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }

    // tests group_result
    @Test
    public void withGroup() {
        if (VSR_KEY.isEmpty() || SBI_KEY.isEmpty())
            return;
        try {
            SearchByImageParam param = SearchByImageParam.newFromImageUrl(IMG_URL);
            param.setGroupBy("brand_name");
            param.setGroupLimit(10);

            ProductSearch sdk = getProductSearch(SBI_KEY, SBI_PLACEMENT);
            ProductSearchResponse response = sdk.imageSearch(param);

            assertEquals(response.getGroupByKey().isEmpty(), false);
            assertEquals(response.getGroupProductResults().isEmpty(), false);

        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }

    // tests objects
    @Test
    public void withSearchAll() {
        if (VSR_KEY.isEmpty() || SBI_KEY.isEmpty())
            return;
        try {
            SearchByImageParam param = SearchByImageParam.newFromImageUrl(IMG_URL);

            param.setSearchAllObjects(true);
            param.setDetectionLimit(10);
            param.setDetection("all");

            ProductSearch sdk = getProductSearch(SBI_KEY, SBI_PLACEMENT);
            ProductSearchResponse response = sdk.imageSearch(param);

            assertEquals(response.getObjects().isEmpty(), false);

        } catch (InternalViSearchException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testVsrReturnProduct() {
        if (VSR_KEY.isEmpty()) return;

        SearchByIdParam param = new SearchByIdParam("POMELO2-AF-SG_93383a6af75493ff78b7ccccf86b848d150c7d4f");
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);
        param.setReturnProductInfo(true);
        param.setAttrsToGet(Arrays.asList("sku","brand_name","sale_date","merchant_category"));

        // execute search and return response
        ProductSearch sdk = getProductSearch(VSR_KEY, VSR_PLACEMENT);
        ProductSearchResponse searchResponse = sdk.visualSimilarSearch(param);

        Product product = searchResponse.getProduct();
        System.out.println("product: ");
        System.out.println("productId: " + product.getProductId());
        System.out.println("main_image_url: " + product.getMainImageUrl());
        System.out.println("data: " + product.getData());
        System.out.println("sale_end_date: " + product.getData().get("sale_end_date").asString());
        System.out.println("sale_price: " + product.getData().get("sale_price").asStringStringMap());
        System.out.println("merchant_category: " + product.getData().get("merchant_category").asStringList());
    }

    @Test
    public void testRecommendationReturnProduct() {
        if (VSR_KEY.isEmpty()) return;

        SearchByIdParam param = new SearchByIdParam("POMELO2-AF-SG_93383a6af75493ff78b7ccccf86b848d150c7d4f");
        param.setShowScore(true);
        param.setReturnFieldsMapping(true);
        param.setReturnProductInfo(true);
        param.setAttrsToGet(Arrays.asList("sku","brand_name","sale_date","merchant_category"));
        param.setAlt_limit(3);

        // execute search and return response
        ProductSearch sdk = getProductSearch(VSR_KEY, VSR_PLACEMENT);
        ProductSearchResponse searchResponse = sdk.recomendation(param);

        Product product = searchResponse.getProduct();
        System.out.println("product: ");
        System.out.println("productId: " + product.getProductId());
        System.out.println("main_image_url: " + product.getMainImageUrl());
        System.out.println("data: " + product.getData());
        System.out.println("sale_end_date: " + product.getData().get("sale_end_date").asString());
        System.out.println("sale_price: " + product.getData().get("sale_price").asStringStringMap());
        System.out.println("merchant_category: " + product.getData().get("merchant_category").asStringList());
    }
}