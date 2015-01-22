package com.visenze.visearch.internal;

import com.visenze.visearch.*;

public interface SearchOperations {

    PagedSearchResult<ImageResult> search(SearchParams searchParams);

    PagedSearchResult<ImageResult> colorSearch(ColorSearchParams colorSearchParams);

    PagedSearchResult<ImageResult> uploadSearch(UploadSearchParams uploadSearchParams);

    PagedSearchResult<ImageResult> uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings);

}
