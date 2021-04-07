# Product Search API

---

## Table of Contents
1. [Introduction](#1-introduction)
    - 1.1 [Terminologies](#11-terminologies)
2. [APIs](#2-apis)
    - 2.1 [Search by Image](#21-search-by-image)
      - 2.1.1 [Using Image URL](#211-using-image-url)
      - 2.1.2 [Using Image File](#212-using-image-file)
      - 2.1.3 [Using Image ID](#213-using-image-id)
    - 2.2 [Search by ID](#22-search-by-id)
3. [Search Results](#3-search-results)
    - 3.1 [Product](#31-product)
4. [Basic Search Parameters](#4-basic-search-parameters)
5. [Advanced Search Parameters](#5-advanced-search-parameters)
    - 5.1 [Grouping](#51-grouping)
    - 5.2 [Filtering](#52-filtering)
    - 5.3 [Visual Analytics](#53-visual-analytics)
    
---

## 1. Introduction

With the release of ViSenze's Catalog system, ViSearch Java SDK will now include
the Product Search API suite - an additional set of APIs that ideally can:

- Select the right product image for indexing for best search performance
- Aggregate search results on a product level instead of image level
- Consistent data type in API response with Catalog’s schema

### 1.1 Terminologies 

Here are some terminologies that will provide background context and help with 
better understanding of Product Search APIs.

| Term          | Summary                                                      |
| ------------- | ------------------------------------------------------------ | 
| App           | Apps that perform certain search or recommendation capabilities, such as visual search, complete the look and so on.           |
| Catalog       | Centralized data storage for customers to ingest their data  |
| Placement     | A location where the customer wants to use the app. They may split this according to their needs, e.g. via platform (ios, android, web), location on their platform (home page, pdp, etc)

Each placement has to be associated with an integration type: API, SDK or widget. Reporting is also viewable by placement.
                  |

## 2. APIs

There are two main APIs provided in this suite, one allows searching for 
products based on an image input, the other searches using a product's ID. A 
product's ID can be retrieved from a [Search Result](#3-search-results).

### 2.1 Search by Image

POST /v1/product/search_by_image 

This API allows you to search the Catalog for products that are similar to the 
Image you provide. You can provide an Image to the API using the following 
methods:

#### 2.1.1 Using Image URL

````
// This is a sample url, replace it with any image url you want
String url = "https://img.ltwebstatic.com/images2_pi/2019/09/09/15679978193855617200_thumbnail_900x1199.jpg";

// Create the parameter object with the image url
SearchByImageParam params = SearchByImageParam.newFromImageUrl(url);

// Create the ProductSearch object
// Replace APP_KEY, PLACEMENT_ID with your App's key and Placement ID as seen in your dashboard
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
                     .build();

// Use the ProductSearch object to perform searches, it will return a
// ProductSearchResponse object that contains all the searched results info 
ProductSearchResponse searchResults = api.imageSearch(params);                     
````

#### 2.1.2 Using Image File

````
// Create a File object using your image's file path
File imageFile = new File(FILE_PATH_TO_IMAGE);

// Create the parameter object with the image file 
SearchByImageParam params = SearchByImageParam.newFromImageFile(imageFile);

// Create the ProductSearch object
// Replace APP_KEY, PLACEMENT_ID with your App's key and Placement ID as seen in your dashboard
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
                     .build();

// Use the ProductSearch object to perform searches, it will return a
// ProductSearchResponse object that contains all the searched results info 
ProductSearchResponse searchResults = api.imageSearch(params);                     
````

#### 2.1.3 Using Image ID

````
      .
      .
      .
// Assuming that you already have a search response stored in 'searchResults'
// Retrieve the search results' image ID. This ID belongs to the image that was 
// used to perform the search. Using the image ID means we are searching with  
// the same image as before, without reuploading the image.
String imageID = searchResults.getImageId();

// Create the parameter object with the image ID 
SearchByImageParam param = SearchByImageParam.newFromImageId(imageID);

// Assuming you have created the ProductSearch object to perform an initial
// search request previously - named 'api'. Re-use it to call another search
// with the new parameters containing imageID
searchResults = api.imageSearch(params);  

````

> More parameters can be provided for further customization - see [Basic](#4-basic-search-parameters) 
> and [Advanced](#5-advanced-search-parameters) search parameters.

### 2.2 Search by ID

GET /v1/product/search_by_id/PRODUCT_ID

This API is **NOT** the same as [Search by Image using Image ID](#213-using-image-id). 
The ID referred to in this API is the **product's ID** - a unique identifier 
tied to each product in the Catalog.

In [Search Results](#3-search-results) section, you will learn how to retrieve
a product's ID after performing a search. That ID can then be used in this API
method:

```
// Assuming you know the product ID of a desired product to be searched against
String productID = PRODUCT_ID;

// Create the parameter object with the image file 
SearchByIdParam params = new SearchByIdParam(productID);

// Create the ProductSearch object
// Replace APP_KEY, PLACEMENT_ID with your App's key and Placement ID as seen in your dashboard
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
                     .build();
                     
// Use the ProductSearch object to perform searches, it will return a
// ProductSearchResponse object that contains all the searched results info 
ProductSearchResponse searchResults = api.visualSimilarSearch(params);  
```

> pseudo steps:
> - make an initial search using an image (file, url)
> - store the search results (contains the list of products)
> - use the product ID of one of these products in the next search

## 3. Search Results

After performing searches with any of the methods above, they will all return 
the same object type of [ProductSearchResponse.java](ProductSearchResponse.java). 
The main purpose of this object is for users to retrieve information of the 
search result, it should **NOT** require you to use any of its setter methods.

Most of the member variables are self-explanatory with additional comments that 
can be seen in the class itself. The more complicated one would be the 
'result' member variable which is a **List of [Product](response/Product.java)**. 

When a search is made, the API will take reference from an Image source (the 3 
methods mentioned in [Search by Image](#21-search-by-image)), and return a list 
of products in the Catalog that is similar to the image provided. If there was 
no status or error code that indicates a failed search, you can access the list
of products, and it's product ID:
```
// the list of all products similar to the image sent as search 
List<Product> products = searchResult.getResult();

// go through each product in the list of products
for (Product product : products) {
   // This is the product ID that can be used in SearchByIdParam
   String productID = product.getProductId();
}
```

### 3.1 Product 

The [Product.java](response/Product.java)
class holds all the information regarding a single Product, with the most
complicated member variable being 'data'. To better explain what this 'data'
field is, take a look at the table below (database field_names):

|ViSenze pre-defined catalog fields|Client X's catalog original names
|---|---|
|product\_id|sku|
|main\_image\_url|medium_image|
|title|product_name|
|product\_url|link|
|price|sale\_price|
|brand|brand|

The above table is a representation of how ViSenze's Catalog name its fields vs
how Client X's database name its fields - both fields essentially mean the same 
thing just named differently.

> i.e. visenze_database["product\_id"] == client_x_database["sku"]

This table can be found in the [ProductSearchResponse.java](ProductSearchResponse.java):

``` 
Map<String,String> catalogFieldsMapping = searchResult.getCatalogFieldsMapping()
```

This catalog fields mapping is actually used in the 'data' member variable which
is a Map<String, ViJsonAny>. The keys in this map corresponds to the above
table's 'Client X's database keys'. An example to retrieve a product's price:

``` 
// assuming you have a searchResult already, get the list of all products 
List<Product> products = searchResult.getResult();

// get the catalog field mapping to know how to translate key names
Map<String,String> catalogFieldsMap = searchResult.getCatalogFieldsMapping();

// get the client's key name that represents price
String price_key = catalogFieldsMap.get("price");

// go through each product in the list of products
for (Product product : products) {
   // get the price of the product
   ViJsonAny price = product.getData().get(price_key);
}
```

Now if you notice the example code above, we have successfully retrieved the 
price of a product regardless of how any client would name their price key. 
However, the return type is ViJsonAny and not a number. This is because there 
are times where the data field might be a list, or even a map. Here are some
examples:

> price : {"currency" : "SGD","value" : "120"},
> 
> colors : [ "blue", "red" ]
> 
> brand : "Gucci"

In the above example, price is a Map<String, String>, colors is a List</String/>
and brand is a String. Since they are all stored in 'data' map, we need a 
general way to store all of them (type-erasure). ViJsonAny wraps the JsonNode to 
allow more readable way of accessing data. Here is how we can convert the price
field back from a ViJsonAny to a Map<String,String>:

``` 
for (Product product : products) {
   // continuing from the code snippet example above
   ViJsonAny price = product.getData().get(price_key);
   
   // retrieve as map
   Map<String,String> originalData = price.asStringStringMap();
   originalData.get("currency"); // "SGD"
   originalData.get("value"); // "120"
}
```

As String:
```
ViJsonAny any = ...
String s = any.asString();
```
As List of Strings:
```
ViJsonAny any = ...
List<String> list = any.asStringList();
```

## 4. Basic Search Parameters

Majority of it can be referred back to [ViSearch Java SDK](../../../../../../README.md).
Here are some common and perhaps confusing ones to take note of:

<table>
  <tr>
    <td>Class</td>
    <td>Variable</td>
    <td>Description</td>
  </tr>
  <tr>
    <td rowspan="8">BaseProductSearchParam</td>
    <td>returnFieldsMapping</td>
    <td>Set to true to get catalog_fields_mapping from API</td>
  </tr>
  <tr> <td>filters</td> <td rowspan="5">Client's keys and not ViSenze's. Use the catalogFieldsMapping as a look-up table.</td> </tr>
  <tr> <td>textFilters</td> </tr>
  <tr> <td>attrsToGet</td> </tr>
  <tr> <td>sortBy</td> </tr>
  <tr> <td>groupBy</td> </tr>
  <tr> <td>colorRelWeight</td> <td>Wrapper to vs_color_rel_weight parameter. To disable color relevance, set to 0
If not set, will set to system default which means support color relevance for SBI fashion app type (sbi_fashion, vsr_fashion)</td> </tr>
  <tr> <td>returnQuerySysMeta</td> <td>Default to false, if set to true will return system metadata for query image or product which include S3 URL copy, detect, keyword</td> </tr>
  <tr>
    <td rowspan="10">SearchByImageParam</td>
    <td>imUrl</td>
    <td rowspan="3">One of these must be valid, use one of the newFromImage...() static method to construct this class </td>
  </tr>
  <tr> <td>imId</td> </tr>
  <tr> <td>image</td> </tr>
  <tr> <td>searchAllObjects</td> <td>Default to false. If set to true, query response will return all objects (same as ViSearch /discoversearch) </td>  </tr>
  <tr> <td>box</td> <td>Optional parameter for restricting the image area x1,y1,x2,y2. The upper-left corner of an image is (0,0) </td> </tr>
  <tr> <td>detection</td> <td>Turn on automatic object detection so the algorithm will try to detect the object in the image and search</td> </tr>
  <tr> <td>detectionLimit</td> <td>Maximum number of products could be detected for a given image, default value is 5. Values must range between 1-30. Returns the objects with higher confidence score first.</td> </tr> 
  <tr> <td>detectionSensitivity</td> <td>Parameter to set the detection to more or less sensitive. Default is low</td> </tr>  
  <tr> <td>searchAllObjects</td> <td>Defaulted to false, if this is set to true, API will return all objects</td> </tr>  
  <tr> <td>point</td> <td> Levis support </td> </tr>   
<tr>
    <td rowspan="2">SearchByIdParam</td>
    <td>productId</td>
    <td>Must be a valid Product ID, it will be used to append to the GET request path </td>
  </tr>
  <tr>
    <td> returnProductInfo </td>  
    <td> If set to true, query response will return the query product's metadata </td> 
  </tr>
</table>

## 5. Advanced Search Parameters

### 5.1 Grouping

Refer back to [ViSearch Java SDK](../../../../../../README.md)

### 5.2 Filtering

Refer back to [ViSearch Java SDK](../../../../../../README.md)

### 5.3 Visual Analytics

> TODO : To be updated once analytics is done
