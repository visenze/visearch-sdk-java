#ViSearch Java SDK

[![Build Status](https://api.travis-ci.org/visenze/visearch-sdk-java.svg?branch=master)](https://travis-ci.org/visenze/visearch-sdk-java)
[![Coverage Status](https://coveralls.io/repos/visenze/visearch-sdk-java/badge.svg)](https://coveralls.io/r/visenze/visearch-sdk-java)

---

##Table of Contents
 1. [Overview](#1-overview)
 2. [Setup](#2-setup)
 3. [Initialization](#3-initialization)
 4. [Indexing Images](#4-indexing-images)
	  - 4.1 [Indexing Your First Images](#41-indexing-your-first-images)
	  - 4.2 [Image with Metadata](#42-image-with-metadata)
	  - 4.3 [Updating Images](#43-updating-images)
	  - 4.4 [Removing Images](#44-removing-images)
	  - 4.5 [Check Indexing Status](#45-check-indexing-status)
 5. [Searching Images](#5-searching-images)
	  - 5.1 [Pre-indexed Search](#51-pre-indexed-search)
	  - 5.2 [Color Search](#52-color-search)
	  - 5.3 [Upload Search](#53-upload-search)
	    - 5.3.1 [Selection Box](#531-selection-box)
 6. [Search Results](#6-search-results)
 7. [Advanced Search Parameters](#7-advanced-search-parameters)
	  - 7.1 [Retrieving Metadata](#71-retrieving-metadata)
	  - 7.2 [Filtering Results](#72-filtering-results)
	  - 7.3 [Result Score](#73-result-score)
 8. [Code Samples](#8-code-samples)

---

##1. Overview

ViSearch is an API that provides accurate, reliable and scalable image search. ViSearch API provides endpoints that let developers index their images and perform image searches efficiently. ViSearch API can be easily integrated into your web and mobile applications. For more details, see [ViSearch API Documentation](http://www.visenze.com/docs).

The ViSearch Java SDK is an open source software for easy integration with your Java back-end applications and services. For source code and references, visit the [Github Repository](https://github.com/visenze/visearch-sdk-java).

 * Current stable version: 1.2.2
 * Minimum JDK version: 1.6

##2. Setup

For Maven projects, include the dependency in ```pom.xml```:
```
<dependency>
  <groupId>com.visenze</groupId>
  <artifactId>visearch-java-sdk</artifactId>
  <version>1.2.2</version>
</dependency>
```

For Gradle projects, include this line in your ```build.gradle``` dependencies block:
```
compile 'com.visenze:visearch-java-sdk:1.2.2'
```

For SBT projects, add the following line to ```build.sbt```:
```
libraryDependencies += "com.visenze" % "visearch-java-sdk" % "1.2.2"
```

##3. Initialization

To start using ViSearch API, initialize ViSearch client with your ViSearch API credentials. Your credentials can be found in [ViSearch Dashboard](https://dashboard.visenze.com):
```java
ViSearch client = new ViSearch("access_key", "secret_key");
```

##4. Indexing Images

###4.1 Indexing Your First Images

Built for scalability, ViSearch API enables fast and accurate searches on high volume of images. Before making your first image search, you need to prepare a list of images and index them into ViSearch by calling the ```insert``` endpoint. Each image must have a distinct name (```im_name```) which serves as this image's unique identifier and a publicly downloadable URL (```im_url```). ViSearch will fetch and index your images from the given URLs. You can check the status of this process using instructions described in [Section 4.5](#45-check-indexing-status). After the image indexes are built, you can start searching for [similar images using the unique identifier](#51-pre-indexed-search), [using a color](#52-color-search), or [using another image](#53-upload-search).

To index your images, prepare a list of Images and call the ```insert``` endpoint. 
```java
// the list of images to be indexed
List<Image> images = new ArrayList<Image>();
// the unique identifier of the image 'im_name'
String imName = "red_dress";
// the publicly downloadable url of the image 'im_url'
String imUrl = "http://mydomain.com/images/red_dress.jpg";
images.add(new Image(imName, imUrl));
// calls the insert endpoint to index the image
client.insert(images);
```
 > Each ```insert``` call to ViSearch accepts a maximum of 100 images. We recommend indexing your images in batches of 100 for optimized image indexing speed.

Note that error messages may be generated from ```insert``` endpoint call, you can check if this happens using the corresponding insert transection.

```java
List<Image> images = new ArrayList<Image>();
String imName = "red_dress";
String imUrl = "http://mydomain.com/images/red_dress.jpg";
images.add(new Image(imName, imUrl));

// index the image and get the InsertTrans
InsertTrans trans = client.insert(images);
// check if the insert endpoint reports any errors
System.out.println(trans.getTotal() + " succeed and " + trans.getErrorList().size() + " fail");
System.out.println("Error list: ");
for (int i = 0; i < trans.getErrorList().size(); i++) {
    System.out.println(trans.getErrorList().get(i));
} 
```

###4.2 Image with Metadata

Images usually come with descriptive text or numeric values as metadata, for example:

 - title, description, category, brand, and price of an online shop listing image
 - caption, tags, geo-coordinates of a photo

ViSearch combines the power of text search with image search. You can index your images with metadata, and leverage text based query and filtering for even more accurate image search results, for example:

 - limit results within a price range
 - limit results to certain tags, and some keywords in the captions

For detailed references for retrieving metadata and filtering search results, see [Advanced Search Parameters](#7-advanced-search-parameters).

 > To index your images with metadata, first you need to configure the metadata schema in [ViSearch Dashboard](https://dashboard.visenze.com). You can add or remove metadata keys, and modify the metadata types to suit your needs.

Let's assume you have the following metadata schema configured:

| Name | Type | Searchable |
| ---- | ---- | ---------- |
| title | string | true |
| description | text | true |
| price | float | true |

Then index your image with title, description and price:
```java
List<Image> images = new ArrayList<Image>();
String imName = "vintage_wingtips";
String imUrl = "http://mydomain.com/images/vintage_wingtips.jpg";

// add metadata to your image
Map<String, String> metadata = new HashMap<String, String>();
metadata.put("title", "Vintage Wingtips");
metadata.put("description", "A pair of high quality leather wingtips");
metadata.put("price", "100.0");
images.add(new Image(imName, imUrl, metadata));
client.insert(images);
```

> Metadata keys are case-sensitive, and metadata without a matching key in the schema will not be processed by ViSearch. Make sure to configure metadata schema in [ViSearch Dashboard](https://dashboard.visenze.com) for all of your metadata keys.

###4.3 Updating Images

If you need to update an image or its metadata, call the ```insert``` endpoint with the same unique identifier of the image. ViSearch will fetch the image from the updated URL and index the new image, and replace the metadata of the image if provided. 

```java
List<Image> images = new ArrayList<Image>();
// the unique identifier 'im_name' of a previously indexed image
String imName = "vintage_wingtips";
// the new url of the image
String imUrl = "http://mydomain.com/images/vintage_wingtips_sale.jpg";

// update metadata of the image
Map<String, Object> metadata = new HashMap<String, Object>();
metadata.put("title", "Vintage Wingtips Sale");
metadata.put("price", "69.99");

images.add(new Image(imName, imUrl, metadata));
client.insert(images);
```

 > Each ```insert``` call to ViSearch accepts a maximum of 100 images. We recommend updating your images in batches of 100 for optimized image indexing speed.

### 4.4 Removing Images

In case you decide to remove some of the indexed images, you can call the ```remove``` endpoint with the list of unique identifier of the indexed images. ViSearch will then remove the specified images from the index.

```java
// the list of unique identifiers 'im_name' of the images to be removed
List<String> removeList = new ArrayList<String>();
// removing previously indexed image "red_dress"
removeList.add("red_dress");
client.remove(removeList);
```
 > We recommend calling ```remove``` in batches of 100 images for optimized image indexing speed.

###4.5 Check Indexing Status

The fetching and indexing process take time, and you may only search for images after their indexs are built. If you want to keep track of this process, you can call the ```insertStatus``` endpoint with the image's trasaction identifier.

```java
List<Image> images = new ArrayList<Image>();
String imName = "vintage_wingtips";
String imUrl = "http://mydomain.com/images/vintage_wingtips.jpg";
images.add(new Image(imName, imUrl));

// index the image and get the InsertTrans
InsertTrans trans = client.insert(images);

InsertStatus status;
// check the status of indexing process while it is not complete
int percent = 0;
while (percent < 100) {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    status = client.insertStatus(trans.getTransId());
    percent = status.getProcessedPercent();
    System.out.println(percent + "% complete");
}

int pageIndex = 1; // error page index always starts with 1
int errorPerPage = 10;  // set error page limit
status = client.insertStatus(trans.getTransId(), pageIndex, errorPerPage);
System.out.println("Start time:" + status.getStartTime());
System.out.println("Update time:" + status.getUpdateTime());
System.out.println(status.getTotal() + " insertions with "
        + status.getSuccessCount() + " succeed and "
        + status.getFailCount() + " fail");

// print all the error messages if there are any
if (status.getFailCount() > 0) {
    int totPageNumber = (int) Math.ceil(1.0 * status.getFailCount() / status.getErrorLimit());
    for (pageIndex = 1; pageIndex <= totPageNumber; pageIndex++) {
        status = client.insertStatus(trans.getTransId(), pageIndex, errorPerPage);
        List<InsertError> errorList = status.getErrorList();
        for (int errorIndex = 0; errorIndex < errorList.size(); errorIndex++) {
            System.out.println("failure at page " + pageIndex
                    + " with error message: " + errorList.get(errorIndex));
        }
    }
}
```

##5. Searching Images

###5.1 Pre-indexed Search

Searches for similar images based on the your indexed image by its unique identifier:

```java
SearchParams params = new SearchParams("vintage_wingtips");
PagedSearchResult searchResult = client.search(params);
```

###5.2 Color Search

Searches for images related to a color by providing a hexadecimal RGB color code:

```java
ColorSearchParams params = new ColorSearchParams("9b351b");
PagedSearchResult searchResult = client.colorSearch(params);
``` 

###5.3 Upload Search

Searches for similar images by uploading an image or providing an image URL:

 - Using an image from a local file path:
```java
File imageFile = new File("/path/to/your/image");
UploadSearchParams params = new UploadSearchParams(imageFile);
PagedSearchResult searchResult = client.uploadSearch(params);
```

 - Using image url:
```java
String url = "http://mydomain.com/sample_image.jpg";
UploadSearchParams params = new UploadSearchParams(url);
PagedSearchResult searchResult = client.uploadSearch(params);
```

####5.3.1 Selection Box

If the object you wish to search for takes up only a small portion of your image, or if other irrelevant objects exists in the same image, chances are the search result could become inaccurate. Use the Box parameter to refine the search area of the image to improve accuracy. The box coordinates are set with respect to the original size of the uploading image:
(note: if the box coordinates are invalid(negative or exceed the image boundary), this search will be equivalent to the normal Upload Search)

```java
File imageFile = new File("/path/to/your/image");
UploadSearchParams params = new UploadSearchParams(imageFile);
// create the box to refine the area on the searching image
// Box(x1, y1, x2, y2) where (0, 0) is the top-left corner
// of the image, (x1, y1) is the top-left corner of the box,
// and (x2, y2) is the bottom-right corner of the box.
Box box = new Box(50, 50, 200, 200);
params.setBox(box);
PagedSearchResult searchResult = client.uploadSearch(params);
```

##6. Search Results

ViSearch returns a maximum number of 1000 most relevant image search results. You can provide pagination parameters to control the paging of the image search results.

Pagination parameters:

| Name | Type | Description |
| ---- | ---- | ----------- |
| page | Integer | Optional parameter to specify the page of results. The first page of result is 1. Defaults to 1. |
| limit | Integer | Optional parameter to specify the result per page limit. Defaults to 10. |

```java
// building pre-indexed search params
SearchParams params = new SearchParams("vintage_wingtips");
params.setPage(1);
params.setLimit(20);
PagedSearchResult searchResult = client.search(params);

// total number of results
int total = searchResult.getTotal();
// get the list of image search results
List<ImageResult> imageResults = searchResult.getResult();
// iterates through the list and get unique identifiers of the results
for (ImageResult imageResult : imageResults) {
    String imName = imageResult.getImName();
    // your code follows
}

// if more results available, get the next page of results
params.setPage(2);
PagedSearchResult nextPageOfSearchResult = client.search(params);
```


##7. Advanced Search Parameters

###7.1 Retrieving Metadata

To retrieve metadata of your image results, provide the list of metadata keys for the metadata value to be returned in the `fl` (field list) property:

```java
SearchParams params = new SearchParams("vintage_wingtips");
// add fq param to specify the list of metadata to retrieve
List<String> fl = new ArrayList<String>();
fl.add("title");
fl.add("price");
params.setFl(fl);
PagedSearchResult searchResult = client.search(params);
List<ImageResult> imageResults = searchResult.getResult();
for (ImageResult imageResult : imageResults) {
    Map<String, String> metadata = imageResult.getMetadata();
    // read your metadata here
}
```

To retrieve all metadata of your image results, specify ```get_all_fl``` parameter and set it to ```true```:

```java
SearchParams params = new SearchParams("vintage_wingtips");
params.setGetAllFl(true);
PagedSearchResult searchResult = client.search(params);
List<ImageResult> imageResults = searchResult.getResult();
for (ImageResult imageResult : imageResults) {
    Map<String, String> metadata = imageResult.getMetadata();
    // read your metadata here
}
```

 > Only metadata of type string, int, and float can be retrieved from ViSearch. Metadata of type text is not available for retrieval.

###7.2 Filtering Results

To filter search results based on metadata values, provide a map of metadata key to filter value in the `fq` (filter query) property:

```java
SearchParams params = new SearchParams("vintage_wingtips");
// add fq param to specify the filtering criteria
Map<String, String> fq = new HashMap<String, String>();
// description is metadata type text
fq.put("description", "wingtips");
// price is metadata type float
fq.put("price", "0,199");
params.setFq(fq);
PagedSearchResult searchResult = client.search(params);
```

Querying syntax for each metadata type is listed in the following table:

Type | FQ
--- | ---
string | Metadata value must be exactly matched with the query value, e.g. "Vintage Wingtips" would not match "vintage wingtips" or "vintage"
text | Metadata value will be indexed using full-text-search engine and supports fuzzy text matching, e.g. "A pair of high quality leather wingtips" would match any word in the phrase
int | Metadata value can be either: <ul><li>exactly matched with the query value</li><li>matched with a ranged query ```minValue,maxValue```, e.g. int value 99 would match ranged query ```0,199```</li></ul>
float | Metadata value can be either <ul><li>exactly matched with the query value</li><li>matched with a ranged query ```minValue,maxValue```, e.g. float value 99.99 would match ranged query ```0.0,199.99```</li></ul>

###7.3 Result Score

ViSearch image search results are ranked in descending order i.e. from the highest scores to the lowest, ranging from 1.0 to 0.0. By default, the score for each image result is not returned. You can turn on the ```score``` property to retrieve the scores for each image result:

```java
SearchParams params = new SearchParams("vintage_wingtips");
// return scores for each image result, default is false
params.setScore(true);
PagedSearchResult searchResult = client.search(params);
List<ImageResult> imageResults = searchResult.getResult();
for (ImageResult imageResult : imageResults) {
    float score = imageResult.getScore();
    // do something with the score
}
```

If you need to restrict search results from a minimum score to a maximum score, specify the ```score_min``` and/or ```score_max``` parameters:

Name | Type | Description
---- | ---- | -----------
score_min | Float | Minimum score for the image results. Default is 0.0.
score_max | Float | Maximum score for the image results. Default is 1.0.

```java
SearchParams params = new SearchParams("vintage_wingtips");
params.setScoreMin(0.5f);
params.setScoreMax(0.8f);
// only retrieve search results with scores between 0.5 and 0.8
PagedSearchResult searchResult = client.search(params);
```