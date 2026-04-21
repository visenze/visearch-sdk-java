## Commands

```bash
# Build
mvn clean install

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=ProductSearchTest

# Run a single test method
mvn test -Dtest=ProductSearchTest#testMethodName

# Build without tests
mvn clean install -DskipTests

# Package JAR
mvn package
```

## Architecture

This is a Java SDK (JDK 1.6+, Maven) providing two APIs:

### ViSearch API (`com.visenze.visearch`)
Legacy image search. Entry point: `ViSearch` — implements both `DataOperations` (index management: insert/remove) and `SearchOperations` (search/upload/color search). Authentication is HTTP Basic Auth (access key + secret key) added via `ViSearchHttpClientImpl`.

### Product Search API (`com.visenze.productsearch`)
Modern product discovery. Entry point: `ProductSearch` — built via `ProductSearch.Builder`. Auth is via `app_key` query param (no header auth). Uses `ProductSearchHttpClientImpl` which extends `ViSearchHttpClientImpl` but skips the Basic Auth header.

`Builder` supports `.useAws()`, `.useAzure()`, and `.setApiEndPoint(url)` to configure the endpoint. When the endpoint is `https://multisearch-aw.rezolve.com` or `https://multisearch-az.rezolve.com`, the SDK selects `NEW_PATHS` (e.g. `/v1/search`, `/v1/visearch/search_by_image`); all other endpoints use `LEGACY_PATHS` (e.g. `/v1/product/multisearch`, `/v1/product/search_by_image`). Path selection is handled by `EndpointPathConfig` — a static inner class in `ProductSearch.java` with two pre-built instances (`LEGACY_PATHS`, `NEW_PATHS`) chosen at construction time via `isNewEndpoint()`.

### Shared Infrastructure
- **`com.visenze.visearch.internal.http`**: Apache HttpComponents-based HTTP layer. `ViSearchHttpClientImpl` handles GET/POST (including multipart for image uploads). `ViSearchHttpResponse` wraps the raw response.
- **`com.visenze.visearch.internal`**: `SearchOperationsImpl` and `DataOperationsImpl` contain endpoint routing and JSON deserialization. `BaseViSearchOperations` holds the shared `ViSearchHttpClient` + `ObjectMapper`.
- **`com.visenze.visearch.internal.json`**: Jackson mixins for deserializing API responses into domain objects (e.g. `ImageResultMixin`, `FacetMixin`). `ViSearchModule` registers all mixins.
- **`com.visenze.common.util`**: `ViJsonAny` — a `JsonNode` wrapper for fields that can be a scalar, list, or map. Used in `Product` and other response models. `MultimapUtil` converts Guava `Multimap` to HTTP params.

### Param → Response Flow
Search params (e.g. `SearchByImageParam`, `SearchParams`) serialize to a `Multimap<String, String>` via `.toMap()`. The HTTP client sends this as query params or form body. Responses deserialize via Jackson into typed result objects (e.g. `ProductSearchResponse`, `PagedSearchResult`).

### Key Endpoints
| API | Constant location | Default base |
|-----|-------------------|--------------|
| ViSearch | `ViSearch.java` | `http://visearch.visenze.com` |
| Product Search (default) | `ProductSearch.java` | `https://search.visenze.com` |
| Product Search (AWS) | `ProductSearch.ENDPOINT_AWS` | `https://multisearch-aw.rezolve.com` |
| Product Search (Azure) | `ProductSearch.ENDPOINT_AZURE` | `https://multisearch-az.rezolve.com` |

### Testing
Tests use JUnit 4 + Mockito + PowerMock. HTTP responses are mocked via `ViSearchHttpClient` stubs. Test JSON fixtures are inline strings within test classes.
