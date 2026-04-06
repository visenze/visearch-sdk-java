# ViSearch API

ViSearch is a legacy image search API available through the [ViSenze Dashboard](https://dashboard.visenze.com). It provides fast, accurate, and scalable image search by letting you index images and query them visually, by color, or by metadata.

This guide covers client setup, indexing images, running searches, and working with results.

> **Note:** If you are using the newer [Rezolve Console](https://ms.console.rezolve.com), use the [Product Search API](./product-search-api.md) instead.

---

## Table of Contents

1. [Initialization](#1-initialization)
2. [Indexing Images](#2-indexing-images)
   - 2.1 [Insert Images](#21-insert-images)
   - 2.2 [Images with Metadata](#22-images-with-metadata)
   - 2.3 [Update Images](#23-update-images)
   - 2.4 [Remove Images](#24-remove-images)
   - 2.5 [Check Indexing Status](#25-check-indexing-status)
3. [Search APIs](#3-search-apis)
   - 3.1 [Visually Similar Recommendations](#31-visually-similar-recommendations)
   - 3.2 [Search by Image](#32-search-by-image)
   - 3.3 [Multiple Product Search](#33-multiple-product-search)
   - 3.4 [Search by Color](#34-search-by-color)
   - 3.5 [Visually Similar Recommendations (Multi-Object Mode)](#35-visually-similar-recommendations-multi-object-mode)
4. [Search Results & Pagination](#4-search-results--pagination)
5. [Advanced Parameters](#5-advanced-parameters)
   - 5.1 [Retrieving Metadata](#51-retrieving-metadata)
   - 5.2 [Filtering Results](#52-filtering-results)
   - 5.3 [Result Score](#53-result-score)
   - 5.4 [Deduplication](#54-deduplication)

---

## 1. Initialization

Initialize the `ViSearch` client with your access and secret keys from the [ViSearch Dashboard](https://dashboard.visenze.com).

```java
// Default endpoint (https://visearch.visenze.com)
ViSearch client = new ViSearch("access_key", "secret_key");

// Custom endpoint
ViSearch client = new ViSearch("https://custom-visearch.yourdomain.com", "access_key", "secret_key");
```

---

## 2. Indexing Images

Before you can run searches, your images need to be indexed. ViSearch fetches each image from a public URL and stores it for fast retrieval. The sections below cover inserting, updating, and removing images.

### 2.1 Insert Images

Each image requires a unique name (`im_name`) and a publicly accessible URL (`im_url`). ViSearch fetches and indexes images from the provided URLs.

```java
List<Image> images = new ArrayList<>();
images.add(new Image("red_dress", "http://mydomain.com/images/red_dress.jpg"));

client.insert(images);
```

> Each `insert` call accepts up to **100 images**. Batch your requests in groups of 100 for best performance.

To handle insert errors:

```java
InsertTrans trans = client.insert(images);

if (trans.getErrorList() != null && trans.getErrorList().size() > 0) {
    System.out.println(trans.getTotal() + " succeeded, " + trans.getErrorList().size() + " failed");
    for (String error : trans.getErrorList()) {
        System.out.println("Error: " + error);
    }
}
```

### 2.2 Images with Metadata

Images can be indexed with descriptive metadata (e.g. title, price, tags). Metadata keys must be configured in advance via the [ViSearch Dashboard](https://dashboard.visenze.com).

Example schema:

| Field       | Type   | Searchable |
|-------------|--------|------------|
| title       | string | true       |
| description | text   | true       |
| price       | float  | true       |

```java
Map<String, String> metadata = new HashMap<>();
metadata.put("title", "Vintage Wingtips");
metadata.put("description", "A pair of high quality leather wingtips");
metadata.put("price", "100.0");

images.add(new Image("vintage_wingtips", "http://mydomain.com/images/vintage_wingtips.jpg", metadata));
client.insert(images);
```

> Metadata keys are case-sensitive. Keys not present in your configured schema will be ignored.

### 2.3 Update Images

Re-insert an image using its existing `im_name` to update its URL or metadata.

```java
Map<String, Object> metadata = new HashMap<>();
metadata.put("title", "Vintage Wingtips Sale");
metadata.put("price", "69.99");

images.add(new Image("vintage_wingtips", "http://mydomain.com/images/vintage_wingtips_sale.jpg", metadata));
client.insert(images);
```

### 2.4 Remove Images

```java
List<String> removeList = new ArrayList<>();
removeList.add("red_dress");

client.remove(removeList);
```

> Recommended batch size: up to 100 images per call.

### 2.5 Check Indexing Status

Images are searchable only after indexing is complete. Use `insertStatus` to track progress.

```java
InsertTrans trans = client.insert(images);

int percent = 0;
while (percent < 100) {
    Thread.sleep(1000);
    InsertStatus status = client.insertStatus(trans.getTransId());
    percent = status.getProcessedPercent();
    System.out.println(percent + "% complete");
}
```

To retrieve detailed error information with pagination:

```java
int pageIndex = 1;
int errorPerPage = 10;
InsertStatus status = client.insertStatus(trans.getTransId(), pageIndex, errorPerPage);

System.out.println("Start time: " + status.getStartTime());
System.out.println("Update time: " + status.getUpdateTime());
System.out.println(status.getTotal() + " total — " + status.getSuccessCount() + " succeeded, " + status.getFailCount() + " failed");

if (status.getFailCount() > 0) {
    int totalPages = (int) Math.ceil(1.0 * status.getFailCount() / status.getErrorLimit());
    for (pageIndex = 1; pageIndex <= totalPages; pageIndex++) {
        status = client.insertStatus(trans.getTransId(), pageIndex, errorPerPage);
        for (InsertError error : status.getErrorList()) {
            System.out.println("Error on page " + pageIndex + ": " + error);
        }
    }
}
```

---

## 3. Search APIs

Once your images are indexed, you can search in several ways — by image similarity, file upload, color, or across multiple detected objects.

### 3.1 Visually Similar Recommendations

`GET /search`

Search for visually similar images using an indexed image's unique identifier (`im_name`).

```java
SearchParams params = new SearchParams("vintage_wingtips");
PagedSearchResult result = client.search(params);
```

### 3.2 Search by Image

`POST /uploadsearch`

Search using an uploaded image or image URL.

```java
// From a local file
File imageFile = new File("/path/to/your/image.jpg");
UploadSearchParams params = new UploadSearchParams(imageFile);

// From a URL
UploadSearchParams params = new UploadSearchParams("http://mydomain.com/sample_image.jpg");

// From a previously used image ID
UploadSearchParams params = new UploadSearchParams();
params.setImId("some_im_id");

PagedSearchResult result = client.uploadSearch(params);
```

> For best results, use images around **1024×1024px**. Maximum file size is **10MB**.

#### Selection Box

Restrict the search to a specific region of the image to improve accuracy when the target object is small or surrounded by irrelevant content.

```java
// Box(x1, y1, x2, y2) — origin (0, 0) is the top-left corner
Box box = new Box(50, 50, 200, 200);
params.setBox(box);
```

### 3.3 Multiple Product Search

`POST /discoversearch`

Detects all objects in the image and returns similar results for each simultaneously.

```java
// From a local file
UploadSearchParams params = new UploadSearchParams(new File("/path/to/image.jpg"));

// From a URL
UploadSearchParams params = new UploadSearchParams("http://mydomain.com/sample_image.jpg");

PagedSearchResult result = client.discoverSearch(params);
```

### 3.4 Search by Color

`GET /colorsearch`

Search for images by hex color code.

```java
// Single color
ColorSearchParams.ColorAndWeight color = new ColorSearchParams.ColorAndWeight("000000");
ColorSearchParams params = new ColorSearchParams(Lists.newArrayList(color));

// Multiple colors with relative weights
ColorSearchParams.ColorAndWeight black = new ColorSearchParams.ColorAndWeight("000000", 50);
ColorSearchParams.ColorAndWeight white = new ColorSearchParams.ColorAndWeight("ffffff", 50);
ColorSearchParams params = new ColorSearchParams(Lists.newArrayList(black, white));

PagedSearchResult result = client.colorSearch(params);
```

### 3.5 Visually Similar Recommendations (Multi-Object Mode)

`GET /match`

Search for visually similar objects in a multi-object index using an indexed image's identifier.

```java
MatchSearchParams params = new MatchSearchParams("im_name");
PagedSearchResult result = client.matchSearch(params);
```

---

## 4. Search Results & Pagination

ViSearch returns up to **1000** results, sorted by relevance. Use `page` and `limit` to paginate.

| Parameter | Type    | Default | Description                    |
|-----------|---------|---------|--------------------------------|
| page      | Integer | 1       | Page number (1-indexed)        |
| limit     | Integer | 10      | Number of results per page     |

```java
SearchParams params = new SearchParams("vintage_wingtips");
params.setPage(1);
params.setLimit(20);

PagedSearchResult result = client.search(params);

int total = result.getTotal();
List<ImageResult> imageResults = result.getResult();

for (ImageResult imageResult : imageResults) {
    String imName = imageResult.getImName();
    // process result
}

// Fetch next page
params.setPage(2);
PagedSearchResult nextPage = client.search(params);
```

---

## 5. Advanced Parameters

These parameters give you finer control over what is returned — including specific metadata fields, filters, relevance scores, and deduplication.

### 5.1 Retrieving Metadata

Specify which metadata fields to include in results using the `fl` (field list) parameter.

```java
SearchParams params = new SearchParams("vintage_wingtips");
params.setFl(Arrays.asList("title", "price"));

PagedSearchResult result = client.search(params);
for (ImageResult imageResult : result.getResult()) {
    Map<String, String> metadata = imageResult.getMetadata();
}
```

To retrieve all metadata fields:

```java
params.setGetAllFl(true);
```

> Only `string`, `int`, and `float` metadata types can be retrieved. `text` type is not available for retrieval.

### 5.2 Filtering Results

Use the `fq` (filter query) parameter to filter results by metadata values.

```java
Map<String, String> fq = new HashMap<>();
fq.put("description", "wingtips");   // text field — fuzzy match
fq.put("price", "0,199");            // float range — min,max

params.setFq(fq);
```

Filter syntax by type:

| Type   | Syntax                                                                 |
|--------|------------------------------------------------------------------------|
| string | Exact match only (case-sensitive)                                      |
| text   | Full-text search with fuzzy matching                                   |
| int    | Exact value or range: `minValue,maxValue`                              |
| float  | Exact value or range: `minValue,maxValue`                              |

### 5.3 Result Score

Results are ranked from highest (1.0) to lowest (0.0) relevance. Scores are not returned by default.

```java
params.setScore(true);

for (ImageResult imageResult : result.getResult()) {
    float score = imageResult.getScore();
}
```

To restrict results to a score range:

```java
params.setScoreMin(0.5f);
params.setScoreMax(0.8f);
```

### 5.4 Deduplication

Remove duplicate images from results based on a similarity threshold.

```java
params.setDedup(true);
params.setDedupThreshold(0.001f);  // lower threshold = stricter deduplication
```

| Parameter      | Default | Description                                              |
|----------------|---------|----------------------------------------------------------|
| dedup          | false   | Enable/disable deduplication                             |
| dedupThreshold | 0.0     | Confidence score difference below which images are dupes |
