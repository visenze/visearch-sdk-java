ViSearch Java SDK
===========
[![Build Status](https://api.travis-ci.org/jasonpeng/visearch-sdk-java.svg?branch=master)](https://travis-ci.org/jasonpeng/visearch-sdk-java)

ViSearch Java SDK
Version: 1.0.0

## Overview
The SDK provides the following APIs for your ViSearch Apps:

* Data API
* Search API

## Setup
You may include the SDK into your project using your favorite build tools:

Maven
```
<dependency>
    <groupId>com.visenze</groupId>
    <artifactId>visearch-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle
```
compile 'com.visenze:visearch-java-sdk:1.0.0'
```

SBT
```
libraryDependencies += "com.visenze" % "visearch-java-sdk" % "1.0.0"
```

## Initialize
```java
ViSearch viSearch = ViSearch.Builder.create("your_access_key", "your_secret_key").build();
```

## Data API
Creates a list of ```Image``` objects:
```java
List<Image> myImages = new List<Image>();
Image myFirstImage = new Image();
myFirstImage.setImName("image1");
myFirstImage.setImUrl("http://myApp.com/image1.jpg");
myImages.add(myFirstImage);
```

Calls ```insert``` to add images to your app:
```java
InsertTransaction insertTransaction = viSearch.insert(myImages);
```

Gets status update calling ```getStatus```:
```java
String id = insertTransaction.getTransactionId();
InsertTransaction updatedInsertTransaction = viSearch.getStatus(id);
Integer total = updatedInsertTransaction.getTotal();
Integer successCount = updatedInsertTransaction.getSuccessCount();
Integer failedCount = updatedInsertTransaction.getFailedCount();
```

### Search API
Build a SearchParams object and call ``search``.
```java
String imName = "myFirstImage";
SearchParams searchParams = new SearchParams(imageId);
PagedSearchResult<ImageResult> pagedSearchResult = viSearch.search(searchParam);
List<ImageResult> imageResults = pagedSearchResult.getResult();
```

Build a ColorSearchParams object and call ``colorSearch``.
```java
String color = "1b3c7e";
ColorSearchParams colorSearchParams = new ColorSearchParams(color);
PagedSearchResult<ImageResult> pagedSearchResult = viSearch.colorSearch(colorSearchParams);
List<ImageResult> imageResults = pagedSearchResult.getResult();
```

Build a UploadSearchParams object and call ``uploadSearch``.
```java
File imageFile = new File("/path/to/your/image");
UploadSearchParams uploadSearchParams = new UploadSearchParams(imageFile);
PagedSearchResult<ImageResult> pagedSearchResult = viSearch.uploadSearch(uploadSearchParams);
List<ImageResult> imageResults = pagedSearchResult.getResult();
```

Configure advanced parameters
```java
String imageId = "myFirstImage";
SearchParams searchParam = new SearchParams("image1");
Map<String, String> fq = new HashMap<>();
fq.put("price", "[100, 200]");
fq.put("brand", "your_brand");
List<String> fl = new ArrayList<>();
fl.add("price");
fl.add("brand");
searchParams.setLimit(50).setPage(3).setFacet(true).setFacetSize(100).setScore(true).setFq(fq).setFl(fl).setQueryInfo(true);
```

## Bug Report
Fork this repository and send a pull request, or raise an issue.