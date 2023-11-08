package com.visenze.visearch.internal;

import com.visenze.visearch.*;

public interface SearchOperations {

    PagedSearchResult search(SearchParams searchParams);

    PagedSearchResult recommendation(RecommendSearchParams searchParams);

    PagedSearchResult colorSearch(ColorSearchParams colorSearchParams);

    PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams);

    PagedSearchResult multiSearch(UploadSearchParams uploadSearchParams);


    PagedSearchResult discoverSearch(UploadSearchParams discoverSearchParams);

    FeatureResponseResult extractFeature(UploadSearchParams uploadSearchParams);

    PagedSearchResult matchSearch(MatchSearchParams searchParams);

    @Deprecated
    PagedSearchResult similarProductsSearch(UploadSearchParams similarProductsSearchParams);
}
