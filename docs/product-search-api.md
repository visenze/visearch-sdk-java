# Product Search API

The Product Search API is part of the [Discovery Suite](https://ms.console.rezolve.com) — a modern product search and recommendations platform built on top of Rezolve's Catalog system. It provides product-level search aggregation, intelligent image selection for indexing, and a consistent schema across all API responses.

This guide covers initialization, the search and recommendations APIs, reading result data, and advanced filtering.

> **Legacy API users:** If you are using the older dashboard, see the [ViSearch API](./visearch-api.md) instead.

---

## Table of Contents

1. [Key Concepts](#1-key-concepts)
2. [Initialization](#2-initialization)
3. [Search APIs](#3-search-apis)
   - 3.1 [Search by Image](#31-search-by-image)
   - 3.2 [Recommendations](#32-recommendations)
4. [Search Results](#4-search-results)
   - 4.1 [Accessing Products](#41-accessing-products)
   - 4.2 [Catalog Field Mapping](#42-catalog-field-mapping)
   - 4.3 [Reading Typed Data](#43-reading-typed-data)
5. [Search Parameters](#5-search-parameters)
   - 5.1 [Common Parameters](#51-common-parameters)
   - 5.2 [Search by Image Parameters](#52-search-by-image-parameters)
   - 5.3 [Recommendations Parameters](#53-recommendations-parameters)
6. [Advanced Parameters](#6-advanced-parameters)
   - 6.1 [Grouping](#61-grouping)
   - 6.2 [Filtering](#62-filtering)

---

## 1. Key Concepts

| Term      | Description                                                                                                                         |
|-----------|-------------------------------------------------------------------------------------------------------------------------------------|
| **App**       | A search or recommendation capability (e.g. visual search, complete the look). Each app performs a specific discovery function. |
| **Catalog**   | Centralized product data storage. Customers ingest their product feed here; the Catalog pushes data to downstream apps.         |
| **Placement** | A named location where an app is deployed (e.g. by platform: iOS/Android/web, or by page: homepage, PDP). Used for reporting.  |

---

## 2. Initialization

`ProductSearch` requires an **App Key** and **Placement ID**, both found in the [Rezolve Console](https://ms.console.rezolve.com) under your app's Integration section.

### Endpoints

| Method | Endpoint | Cloud |
|--------|----------|-------|
| `.useAws()` | `https://multisearch-aw.rezolve.com` | AWS |
| `.useAzure()` | `https://multisearch-az.rezolve.com` | Azure |
| `.setApiEndPoint(url)` | Custom URL | Any / Legacy |

The default endpoint (`https://search.visenze.com`) is used when no endpoint method is called.

```java
// Default endpoint
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
    .build();

// AWS endpoint
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
    .useAws()
    .build();

// Azure endpoint
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
    .useAzure()
    .build();

// Custom endpoint
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
    .setApiEndPoint("https://custom.endpoint.example.com")
    .build();

// Custom client configuration (timeouts, proxy)
ClientConfig config = new ClientConfig();
config.setConnectionTimeout(5000);
config.setSocketTimeout(10000);
config.setProxy(myProxy);

ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID)
    .useAws()
    .setClientConfig(config)
    .build();
```

> **Note:** When using `.useAws()` or `.useAzure()`, the SDK automatically routes each API call to the correct path for that endpoint. Existing code using `.setApiEndPoint(...)` with a legacy URL continues to work unchanged.

---

## 3. Search APIs

The SDK supports two discovery flows: finding products visually similar to an image, and fetching recommendations for a known product.

### 3.1 Search by Image

| Endpoint | Path |
|----------|------|
| Default / Legacy | `POST /v1/product/search_by_image` |
| AWS / Azure | `POST /v1/visearch/search_by_image` |

Search the Catalog for products visually similar to a provided image. Three input methods are supported:

#### Using an Image URL

```java
String url = "https://example.com/images/product.jpg";
SearchByImageParam params = SearchByImageParam.newFromImageUrl(url);

ProductSearchResponse result = api.imageSearch(params);
```

#### Using an Image File

```java
File imageFile = new File("/path/to/product.jpg");
SearchByImageParam params = SearchByImageParam.newFromImageFile(imageFile);

ProductSearchResponse result = api.imageSearch(params);
```

#### Using a Previously Used Image ID

Reuse an image from a prior search without re-uploading it.

```java
String imageId = previousResult.getImageId();
SearchByImageParam params = SearchByImageParam.newFromImageId(imageId);

ProductSearchResponse result = api.imageSearch(params);
```

### 3.2 Recommendations

| Endpoint | Path |
|----------|------|
| Default / Legacy | `GET /v1/product/recommendations/{product_id}` |
| AWS / Azure | `GET /v1/visearch/recommendations/{product_id}` |

Find products similar to a specific product using its Catalog product ID. Product IDs are returned in search results.

```java
// Retrieve a product ID from a prior search
String productId = searchResult.getResult().get(0).getProductId();

// Search for recommendations
SearchByIdParam params = new SearchByIdParam(productId);
ProductSearchResponse result = api.recomendation(params);
```

---

## 4. Search Results

All searches return a `ProductSearchResponse` object. The sections below show how to access products, map catalog fields, and read typed values. See [ProductSearchResponse.java](../src/main/java/com/visenze/productsearch/ProductSearchResponse.java) for the full reference.

### 4.1 Accessing Products

```java
List<Product> products = result.getResult();

for (Product product : products) {
    String productId = product.getProductId();
    // use productId for recommendations, analytics, etc.
}
```

### 4.2 Catalog Field Mapping

Rezolve uses standardized internal field names that map to your catalog's own field names. This means you use your own naming conventions to look up product data, rather than Rezolve's internal ones. For example:

| Catalog Field     | Example Client Field |
|-------------------|----------------------|
| `product_id`      | `sku`                |
| `main_image_url`  | `medium_image`       |
| `title`           | `product_name`       |
| `product_url`     | `link`               |
| `price`           | `sale_price`         |
| `brand`           | `brand`              |

See the [Catalog Data Schema](https://msdocs.rezolve.com/docs/catalog-data-schema) for the complete guide.

The mapping is included in the response:

```java
Map<String, String> fieldMapping = result.getCatalogFieldsMapping();

// Translate Rezolve field name → client's field name
String priceKey = fieldMapping.get("price"); // e.g. "sale_price"
```

Use this translated key to look up data in a `Product`:

```java
for (Product product : products) {
    ViJsonAny price = product.getData().get(priceKey);
}
```

### 4.3 Reading Typed Data

Product data values are returned as `ViJsonAny` to handle varying types (string, list, map). Use the appropriate accessor:

```java
ViJsonAny value = product.getData().get(someKey);

// As a plain string
String s = value.asString();

// As a list of strings (e.g. colors: ["blue", "red"])
List<String> list = value.asStringList();

// As a string-to-string map (e.g. price: {"currency": "SGD", "value": "120"})
Map<String, String> map = value.asStringStringMap();
map.get("currency"); // "SGD"
map.get("value");    // "120"
```

---

## 5. Search Parameters

The following parameters let you control what data is returned, how results are sorted, and what filters are applied.

### 5.1 Common Parameters

These parameters are available on all search requests via `BaseProductSearchParam`:

| Parameter            | Description                                                                                                         |
|----------------------|---------------------------------------------------------------------------------------------------------------------|
| `returnFieldsMapping` | Set to `true` to include `catalog_fields_mapping` in the response                                                 |
| `filters`            | Key-value filters using the **client's** field names (use `catalogFieldsMapping` to look up the correct keys)      |
| `textFilters`        | Same as `filters` but for full-text search fields                                                                   |
| `attrsToGet`         | List of product attribute keys to return in the response                                                            |
| `sortBy`             | Sort results by a specified field                                                                                   |
| `groupBy`            | Group results by a specified field                                                                                  |
| `colorRelWeight`     | Weight for color relevance. Set to `0` to disable. Defaults to system setting (enabled for `sbi_fashion`, `vsr_fashion` app types) |
| `returnQuerySysMeta` | Set to `true` to return system metadata for the query image/product (S3 URL, detected objects, keywords)           |

### 5.2 Search by Image Parameters

These parameters are specific to `SearchByImageParam`:

| Parameter            | Description                                                                                                    |
|----------------------|----------------------------------------------------------------------------------------------------------------|
| `imUrl`              | Image URL to search with                                                                                       |
| `imId`               | Previously used image ID (avoids re-uploading)                                                                 |
| `image`              | Image file to upload                                                                                           |
| `box`                | Restrict search to a region: `x1,y1,x2,y2` (origin is top-left)                                               |
| `detection`          | Enable automatic object detection — the API will detect and search for the main object in the image            |
| `detectionLimit`     | Max number of objects to detect (1–30, default: 5). Higher-confidence objects are returned first               |
| `detectionSensitivity` | Detection sensitivity level. Default is `low`                                                                |
| `searchAllObjects`   | Set to `true` to return results for all detected objects (equivalent to ViSearch's `/discoversearch`)          |
| `point`              | Point coordinate for targeted search                                                                           |

> Use one of the `newFromImage*()` static constructors to ensure `imUrl`, `imId`, or `image` is set correctly.

### 5.3 Recommendations Parameters

These parameters are specific to `SearchByIdParam`:

| Parameter           | Description                                                              |
|---------------------|--------------------------------------------------------------------------|
| `productId`         | A valid product ID from the Catalog (appended to the GET request path)   |
| `returnProductInfo` | Set to `true` to include the query product's own metadata in the response |

---

## 6. Advanced Parameters

Use these parameters to group or filter results after the main query runs.

### 6.1 Grouping

Group search results by a catalog field (e.g. collapse color variants of the same product).

```java
params.setGroupBy("product_group_id");
```

### 6.2 Filtering

Filter results using catalog field values. Use client-side field names (not catalog's internal names).

```java
// Look up the client's key name for a Rezolve catalog field
String priceKey = result.getCatalogFieldsMapping().get("price");

Map<String, String> filters = new HashMap<>();
filters.put(priceKey, "0,200");       // price range
filters.put("brand", "Gucci");        // exact match

params.setFilters(filters);
```
