# ViSearch Java SDK

[![Coverage Status](https://coveralls.io/repos/visenze/visearch-sdk-java/badge.svg)](https://coveralls.io/r/visenze/visearch-sdk-java)

---

- **Current stable version:** 1.14.6
- **Minimum JDK version:** 1.6

This SDK provides two sets of APIs for image search and product discovery:

| API | Dashboard | Description |
|-----|-----------|-------------|
| **[ViSearch](docs/visearch-api.md)** | [dashboard.visenze.com](https://dashboard.visenze.com) | Legacy image search API. Index images and search by image, color, or metadata. |
| **[Product Search](docs/product-search-api.md)** | [ms.console.rezolve.com](https://ms.console.rezolve.com) | Modern product discovery API with catalog-level aggregation, recommendations, and visual analytics. |

---

## Setup

Add the dependency for your build tool. Replace the version with the latest available.

**Maven** (`pom.xml`):
```xml
<dependency>
  <groupId>com.visenze</groupId>
  <artifactId>visearch-java-sdk</artifactId>
  <version>1.14.6</version>
</dependency>
```

**Gradle** (`build.gradle`):
```groovy
compile 'com.visenze:visearch-java-sdk:1.14.6'
```

**SBT** (`build.sbt`):
```scala
libraryDependencies += "com.visenze" % "visearch-java-sdk" % "1.14.6"
```

---

## Quick Start

### ViSearch API

```java
// Default endpoint
ViSearch client = new ViSearch("access_key", "secret_key");

// Custom endpoint
ViSearch client = new ViSearch("https://custom-visearch.yourdomain.com", "access_key", "secret_key");
```

For full documentation see **[docs/visearch-api.md](docs/visearch-api.md)**.

### Product Search API

```java
// Default endpoint
ProductSearch api = new ProductSearch.Builder(APP_KEY, PLACEMENT_ID).build();

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
```

`APP_KEY` and `PLACEMENT_ID` can be found in the [Rezolve Console](https://ms.console.rezolve.com) under your app's Integration section.

For full documentation see **[docs/product-search-api.md](docs/product-search-api.md)**.
