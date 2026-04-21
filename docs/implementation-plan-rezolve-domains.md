# Implementation Plan: Rezolve Domain Update (Product Search API)

## Background

The Product Search API has two new cloud-specific endpoints with different API path structures:

| Endpoint | Cloud |
|----------|-------|
| `https://multisearch-aw.rezolve.com` | AWS |
| `https://multisearch-az.rezolve.com` | Azure |

The existing endpoints (`https://search.visenze.com`, `https://multimodal.search.rezolve.com`) must continue to work unchanged.

## Path Mapping

| Method | Old path (`search.visenze.com`) | New path (`multisearch-*.rezolve.com`) |
|--------|--------------------------------|----------------------------------------|
| `imageSearch` | `/v1/product/search_by_image` | `/v1/visearch/search_by_image` |
| `multiSearch` | `/v1/product/multisearch` | `/v1/search` |
| `multiSearchAutocomplete` | `/v1/product/multisearch/autocomplete` | `/v1/autocomplete` |
| `visualSimilarSearch` | `/v1/product/search_by_id/{id}` | `/v1/visearch/search_by_id/{id}` |
| `recommendations` | `/v1/product/recommendations/{id}` | `/v1/visearch/recommendations/{id}` |

## Design

### Path resolution via `EndpointPathConfig`

Introduce a package-private inner class (or standalone class) `EndpointPathConfig` inside `ProductSearch.java` that groups all path constants into a single value object. Two static instances are created — one for legacy and one for new domains. The correct instance is selected at construction based on the endpoint string.

```java
static class EndpointPathConfig {
    final String imageSearchPath;
    final String multiSearchPath;
    final String multiSearchAutocompletePath;
    final String visualSimilarPath;
    final String recommendationPath;
}

// Legacy config — same as current hardcoded constants
static final EndpointPathConfig LEGACY_PATHS = new EndpointPathConfig(
    "/v1/product/search_by_image",
    "/v1/product/multisearch",
    "/v1/product/multisearch/autocomplete",
    "/v1/product/search_by_id",
    "/v1/product/recommendations"
);

// New rezolve config
static final EndpointPathConfig NEW_PATHS = new EndpointPathConfig(
    "/v1/visearch/search_by_image",
    "/v1/search",
    "/v1/autocomplete",
    "/v1/visearch/search_by_id",
    "/v1/visearch/recommendations"
);
```

### Endpoint detection

Add a private static helper in `ProductSearch`:

```java
private static final Set<String> NEW_ENDPOINTS = new HashSet<>(Arrays.asList(
    "https://multisearch-aw.rezolve.com",
    "https://multisearch-az.rezolve.com"
));

private static boolean isNewEndpoint(String endpoint) {
    // strip trailing slash before comparison
    return NEW_ENDPOINTS.contains(endpoint.replaceAll("/$", ""));
}
```

Selected in the private constructor:

```java
private ProductSearch(String appKey, Integer placementId, String endpoint, ClientConfig config) {
    this.appKey      = appKey;
    this.placementId = placementId;
    this.endpoint    = endpoint;
    this.pathConfig  = isNewEndpoint(endpoint) ? NEW_PATHS : LEGACY_PATHS;
    this.httpClient  = new ProductSearchHttpClientImpl(endpoint, config);
}
```

### Existing methods — no signature changes

All existing public method signatures stay identical. Internally, replace hardcoded path constants with `this.pathConfig.*`:

```java
public ProductSearchResponse imageSearch(SearchByImageParam params) {
    return postImageSearch(params, pathConfig.imageSearchPath);
}

public ProductSearchResponse multiSearch(SearchByImageParam params) {
    return postImageSearch(params, pathConfig.multiSearchPath);
}

public AutoCompleteResponse multiSearchAutocomplete(SearchByImageParam params) {
    return postAutocomplete(params, pathConfig.multiSearchAutocompletePath);
}

public ProductSearchResponse visualSimilarSearch(SearchByIdParam params) {
    return getById(params, pathConfig.visualSimilarPath);
}

public ProductSearchResponse recommendations(SearchByIdParam params) {
    return getById(params, pathConfig.recommendationPath);
}
```

### Builder — no changes

`Builder.setApiEndPoint(String url)` stays exactly as-is. The path selection is entirely internal.

## Files to Change

| File | Change |
|------|--------|
| `ProductSearch.java` | Add `EndpointPathConfig` inner class, `NEW_ENDPOINTS` set, `isNewEndpoint()` helper, `pathConfig` instance field, update all method bodies, add `complementarySearch` and `outfitRecommendations` methods |

## Files to Add / Update (Tests)

| File | Change |
|------|--------|
| `ProductSearchTest.java` | Add test cases verifying correct path is used for new endpoints (mock `httpClient`, assert path passed to `post`/`postImage`/`get`) and that legacy endpoints still use old paths |

## Backwards Compatibility

- Users on `search.visenze.com` or `multimodal.search.rezolve.com`: zero changes required, all existing calls continue to work.
- Users migrating to new endpoints: change `setApiEndPoint(...)` only — method names and param types are unchanged.

## Open Questions

1. Should `Builder` also expose named convenience methods like `.useAws()` / `.useAzure()` as shorthand for `setApiEndPoint(...)`? Purely optional sugar. 

Yes, please add the new methods.

2. The doc table has two identical rows at the bottom (both map `search.visenze.com` → `multisearch-aw.rezolve.com` with no path). Confirm these are duplicates and can be ignored.

Should be ignored. I just removed them.