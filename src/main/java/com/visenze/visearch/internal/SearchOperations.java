package com.visenze.visearch.internal;

import com.visenze.visearch.*;

public interface SearchOperations {

    PagedSearchResult search(SearchParams searchParams);

    PagedSearchResult recommendation(SearchParams searchParams);

    PagedSearchResult colorSearch(ColorSearchParams colorSearchParams);

    PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams);

    PagedSearchGroupResult similarProductsSearch(UploadSearchParams similarProductsSearchPararms);

    /**
     * @deprecated
     * */
    @Deprecated
    PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings);

}
