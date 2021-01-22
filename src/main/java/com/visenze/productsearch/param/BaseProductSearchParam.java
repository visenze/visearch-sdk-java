package com.visenze.productsearch.param;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Optional;
import com.google.common.collect.*;

import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.*;

import java.util.List;
import java.util.Map;

/**
 * <h1> Common Request Parameters </h1>
 * This class holds all the common parameters required by the different search
 * APIs that we support.
 * <p>
 * The java class that wraps any search API should extend from this and only
 * define their own requirements. This class acts as an object that stores the
 * general parameters that is needed, whilst the derived class will store all
 * other specific API parameter requirements.
 * <p>
 * This class aims to be Json compatible by implementing Jackson annotation.
 * Since most variables are Optional<T>, we are going to use their getter method
 * to denote as JsonProperty. This class is only used for posting/sending param
 * and not loading/receiving it, hence no real need to prepare JsonSetters.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class BaseProductSearchParam {

    /**
     * The result page number. Default to 1. Max 50.
     */
    protected Optional<Integer> page = Optional.absent();

    /**
     * The number of results returned per page. The maximum number of results
     * returned from the API is 100. (100 is chosen as this is also DynamoDB
     * batch limit in 1 call)
     */
    protected Optional<Integer> limit = Optional.absent();

    /**
     * Unique string that can identify the user/app, e.g. device serial number,
     * advertising ID or a server generated string (reqid for first search) for
     * analytics purposes.
     */
    protected Optional<String> vaUid = Optional.absent();

    /**
     * Analytics session ID
     */
    protected Optional<String> vaSid = Optional.absent();

    /**
     * For first release, can consider to narrow scope to only product_id,
     * price, category, brand, original_price
     */
    protected Optional<Map<String, String>> filters = Optional.absent();

    /**
     * For fq for text search
     */
    protected Optional<Map<String, String>> textFilters = Optional.absent();

    /**
     * By default we should always show the common fields (title, product_id,
     * main_image_url, title, price, product_url, brand)
     */
    protected Optional<List<String>> attrsToGet = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<List<String>> facets = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Integer> facetsLimit = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Boolean> facetsShowCount = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> sortBy = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> groupBy = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Integer> groupLimit = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> sortGroupBy = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> sortGroupStrategy = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Boolean> score = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Float> scoreMin = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Float> scoreMax = Optional.absent();

    /**
     * Default to false, if set to true, will return catalog fields mappings
     */
    protected Optional<Boolean> returnFieldsMapping = Optional.absent();

    /**
     * Return S3 URL (the copy) of the main product image
     */
    protected Optional<Boolean> returnImageS3Url = Optional.absent();

    /**
     * Which SDK e.g. Javascript, Swift, Android, Java (for server side)
     */
    protected Optional<String> vaSdk = Optional.absent();

    /**
     * Store our tracking SDK’s version e.g. “1.0.2”. For debugging if there is
     * a bug and also to measure SDK popularity/usage
     */
    protected Optional<String> vaSdkVersion = Optional.absent();

    /**
     * Custom mapping for future-proofing
     */
    protected Optional<Map<String, String>> customParams = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaOs = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaOsv = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaDeviceBrand = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaDeviceModel = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaAppBundleId = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaAppName = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaAppVersion = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaAaid = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaDidmd5 = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<Number> vaN1 = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<Number> vaN2 = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaS1 = Optional.absent();

    /**
     * Visenze Analytics fields
     */
    protected Optional<String> vaS2 = Optional.absent();

    /**
     * Convert this object into it's multimap representation.
     *
     * @return multimap of class variable to value
     */
    public Multimap<String, String> toMultimap() {
        Multimap<String, String> multimap = HashMultimap.create();

        if (page.isPresent())
            multimap.put(PAGE, page.get().toString());

        if (limit.isPresent())
            multimap.put(LIMIT, limit.get().toString());

        if (vaUid.isPresent())
            multimap.put(VA_UID, vaUid.get());

        if (vaSid.isPresent())
            multimap.put(VA_SID, vaSid.get());

        if (filters.isPresent()) {
            for (Map.Entry<String, String> entry : filters.get().entrySet())
                multimap.put(FILTERS, entry.getKey() + COLON + entry.getValue());
        }

        if (textFilters.isPresent()) {
            for (Map.Entry<String, String> entry : textFilters.get().entrySet())
                multimap.put(TEXT_FILTERS, entry.getKey() + COLON + entry.getValue());
        }

        if (attrsToGet.isPresent()) {
            for (String val : attrsToGet.get())
                multimap.put(ATTR_TO_GET, val);
        }

        if (facets.isPresent()) {
            for (String val : facets.get())
                multimap.put(FACETS, val);
        }

        if (facetsLimit.isPresent())
            multimap.put(FACETS_LIMIT, facetsLimit.get().toString());

        if (facetsShowCount.isPresent())
            multimap.put(FACETS_SHOW_COUNT, facetsShowCount.get().toString());

        if (sortBy.isPresent())
            multimap.put(SORT_BY, sortBy.get());

        if (groupBy.isPresent())
            multimap.put(GROUP_BY, groupBy.get());

        if (groupLimit.isPresent())
            multimap.put(GROUP_LIMIT, groupLimit.get().toString());

        if (sortGroupBy.isPresent())
            multimap.put(SORT_GROUP_BY, sortGroupBy.get());

        if (sortGroupStrategy.isPresent())
            multimap.put(SORT_GROUP_STRATEGY, sortGroupStrategy.get());

        if (score.isPresent())
            multimap.put(SCORE, score.get().toString());

        if (scoreMin.isPresent())
            multimap.put(SCORE_MIN, scoreMin.get().toString());

        if (scoreMax.isPresent())
            multimap.put(SCORE_MAX, scoreMax.get().toString());

        if (returnFieldsMapping.isPresent())
            multimap.put(RETURN_FIELDS_MAPPING, returnFieldsMapping.get().toString());

        if (returnImageS3Url.isPresent())
            multimap.put(RETURN_IMAGE_S3_URL, returnImageS3Url.get().toString());

        if (vaSdk.isPresent())
            multimap.put(VA_SDK, vaSdk.get());

        if (vaSdkVersion.isPresent())
            multimap.put(VA_SDK_VERSION, vaSdkVersion.get());

        if (customParams.isPresent())
            for (Map.Entry<String, String> entry : customParams.get().entrySet())
                multimap.put(CUSTOM_MAP, entry.getKey() + COLON + entry.getValue());

        if (vaOs.isPresent())
            multimap.put(VA_OS, vaOs.get());

        if (vaOsv.isPresent())
            multimap.put(VA_OSV, vaOsv.get());

        if (vaDeviceBrand.isPresent())
            multimap.put(VA_DEVICE_BRAND, vaDeviceBrand.get());

        if (vaDeviceModel.isPresent())
            multimap.put(VA_DEVICE_MODEL, vaDeviceModel.get());

        if (vaAppBundleId.isPresent())
            multimap.put(VA_APP_BUNDLE_ID, vaAppBundleId.get());

        if (vaAppName.isPresent())
            multimap.put(VA_APP_NAME, vaAppName.get());

        if (vaAppVersion.isPresent())
            multimap.put(VA_APP_VERSION, vaAppVersion.get());

        if (vaAaid.isPresent())
            multimap.put(VA_AAID, vaAaid.get());

        if (vaDidmd5.isPresent())
            multimap.put(VA_DIDMD5, vaDidmd5.get());

        if (vaN1.isPresent())
            multimap.put(VA_N1, vaN1.get().toString());

        if (vaN2.isPresent())
            multimap.put(VA_N2, vaN2.get().toString());

        if (vaS1.isPresent())
            multimap.put(VA_S1, vaS1.get());

        if (vaS2.isPresent())
            multimap.put(VA_S2, vaS2.get());

        return multimap;
    }

    /**
     * Get the result page number
     *
     * @return page number
     */
    @JsonProperty("page")
    public Integer getPage() { return page.orNull(); }

    /**
     * Set the result page number
     *
     * @param page page number to use
     */
    public void setPage(Integer page) {
        // minimum 1
        this.page = Optional.of(page < 1 ? 1 : page);
    }

    /**
     * Get a page's limit
     *
     * @return Number of results per page
     */
    @JsonProperty("limit")
    public Integer getLimit() {
        return limit.orNull();
    }

    /**
     * Set a page's limit
     *
     * @param limit Maximum number of results per page
     */
    public void setLimit(Integer limit) {
        this.limit = Optional.fromNullable(limit);
    }

    /**
     * Get the unique identifier for client application/usage
     *
     * @return vaUid
     */
    @JsonProperty("va_uid")
    public String getVaUid() {
        return vaUid.orNull();
    }

    /**
     * Set the unique identifier for client application/usage
     *
     * @param vaUid uid to identify this app
     */
    public void setVaUid(String vaUid) {
        // dont allow empty string
        this.vaUid = Optional.fromNullable(vaUid);
    }

    /**
     * Get the session ID for analytics
     *
     * @return va_sid
     */
    @JsonProperty("va_sid")
    public String getVaSid() {
        return vaSid.orNull();
    }

    /**
     * Set the session ID for analytics
     *
     * @param vaSid ID to identify this 'session' for analytics
     */
    public void setVaSid(String vaSid) {
        // dont allow empty string
        this.vaSid = Optional.fromNullable(vaSid);
    }

    /**
     * Get the map of filters that will help narrow search scope
     *
     * @return filters to use
     */
    @JsonProperty("filters")
    public Map<String, String> getFilters() {
        return filters.orNull();
    }

    /**
     * Set the map of filters that will help narrow search scope.
     * Conceptual and non-representational example:
     *      filters["category"] = "clothes";
     *      filters["another_filter_key"] = "some_other_filter_value";
     *
     * @param filters A map of filters' key-value
     */
    public void setFilters(Map<String, String> filters) {
        // don't allow empty map
        if (filters == null || filters.isEmpty()) {
            this.filters = Optional.absent();
        }
        // remove null entries
        else {
            // filters.entrySet().removeIf(entries -> entries.getValue() == null);
            this.filters = Optional.of(filters);
        }
    }

    /**
     * Get the filters for text search
     *
     * @return filters for text search
     */
    @JsonProperty("text_filters")
    public Map<String, String> getTextFilters() {
        return textFilters.orNull();
    }

    /**
     * Set the filters for text search
     *
     * @param textFilters filters for text search
     */
    public void setTextFilters(Map<String, String> textFilters) {
        // dont allow empty map
        if (textFilters == null || textFilters.isEmpty()) {
            this.textFilters = Optional.absent();
        }
        // remove null entries
        else {
            // text_filters.entrySet().removeIf(entries -> entries.getValue() == null);
            this.textFilters = Optional.of(textFilters);
        }
    }

    /**
     * Get the attributes required, common fields
     *      i.e. product_id, main_image_url, title
     *
     * @return list of attributes
     */
    @JsonProperty("attrs_to_get")
    public List<String> getAttrsToGet() {
        return attrsToGet.orNull();
    }

    /**
     * Set the attributes required, common fields
     *      i.e. product_id, main_image_url, title
     *
     * @param attrsToGet list of attributes
     */
    public void setAttrsToGet(List<String> attrsToGet) {
        // dont allow empty list
        if (attrsToGet == null || attrsToGet.isEmpty()) {
            this.attrsToGet = Optional.absent();
        }
        else {
            this.attrsToGet = Optional.of(attrsToGet);
        }
    }

    /**
     * Get the facets
     *
     * @return list of facets
     */
    @JsonProperty("facets")
    public List<String> getFacets() {
        return facets.orNull();
    }

    /**
     * Set the facets
     *
     * @param facets list of facets
     */
    public void setFacets(List<String> facets) {
        if (facets.isEmpty()) {
            this.facets = Optional.absent();
        }
        else {
            this.facets = Optional.of(facets);
        }
    }

    /**
     * Get the facets' limit
     *
     * @return facets' limit
     */
    @JsonProperty("facets_limit")
    public Integer getFacetsLimit() {
        return facetsLimit.orNull();
    }

    /**
     * Set the facets' limit
     *
     * @param facetsLimit facets' limit
     */
    public void setFacetsLimit(Integer facetsLimit) {
        this.facetsLimit = Optional.fromNullable(facetsLimit);
    }

    /**
     * Get the facets show count
     *
     * @return should facet show count
     */
    @JsonProperty("facets_show_count")
    public Boolean getFacetsShowCount() {
        return facetsShowCount.orNull();
    }

    /**
     * Set the facets show count
     *
     * @param facetsShowCount should facet show count
     */
    public void setFacetsShowCount(Boolean facetsShowCount) {
        this.facetsShowCount = Optional.fromNullable(facetsShowCount);
    }

    /**
     * Get the 'sort by' string
     *
     * @return criteria of 'sort by'
     */
    @JsonProperty("sort_by")
    public String getSortBy() {
        return sortBy.orNull();
    }

    /**
     * Set the 'sort by' criteria
     *
     * @param sortBy criteria of 'sort by'
     */
    public void setSortBy(String sortBy) {
        this.sortBy = Optional.fromNullable(sortBy);
    }

    /**
     * Get the 'group by' criteria
     *
     * @return criteria of 'group by'
     */
    @JsonProperty("group_by")
    public String getGroupBy() {
        return groupBy.orNull();
    }

    /**
     * Set the 'group by' criteria
     *
     * @param groupBy criteria of 'group by'
     */
    public void setGroupBy(String groupBy) {
        this.groupBy = Optional.fromNullable(groupBy);
    }

    /**
     * Get the group limit
     *
     * @return group limit
     */
    @JsonProperty("group_limit")
    public Integer getGroupLimit() {
        return groupLimit.orNull();
    }

    /**
     * Set the group limit
     *
     * @param groupLimit group limit
     */
    public void setGroupLimit(Integer groupLimit) {
        this.groupLimit = Optional.fromNullable(groupLimit);
    }

    /**
     * Get the 'sort group by'
     *
     * @return criteria for 'sort group by'
     */
    @JsonProperty("sort_group_by")
    public String getSortGroupBy() {
        return sortGroupBy.orNull();
    }

    /**
     * Set the 'sort group by'
     *
     * @param sortGroupBy criteria for 'sort group by'
     */
    public void setSortGroupBy(String sortGroupBy) {
        this.sortGroupBy = Optional.fromNullable(sortGroupBy);
    }

    /**
     * Get the 'sort group by' strategy
     *
     * @return strategy
     */
    @JsonProperty("sort_group_strategy")
    public String getSortGroupStrategy() {
        return sortGroupStrategy.orNull();
    }

    /**
     * Set the 'sort group by' strategy
     *
     * @param sortGroupStrategy sort strategy
     */
    public void setSortGroupStrategy(String sortGroupStrategy) {
        this.sortGroupStrategy = Optional.fromNullable(sortGroupStrategy);
    }

    /**
     * Get score
     *
     * @return score
     */
    @JsonProperty("score")
    public Boolean getShowScore() {
        return score.orNull();
    }

    /**
     * Set score
     *
     * @param score score
     */
    public void setShowScore(Boolean score) {
        this.score = Optional.fromNullable(score);
    }

    /**
     * Get the min score
     *
     * @return minimum score
     */
    @JsonProperty("score_min")
    public Float getScoreMin() {
        return scoreMin.orNull();
    }

    /**
     * Set the min score
     *
     * @param scoreMin minimum score
     */
    public void setScoreMin(Float scoreMin) {
        this.scoreMin = Optional.fromNullable(scoreMin);
    }

    /**
     * Get the max score
     *
     * @return maximum score
     */
    @JsonProperty("score_max")
    public Float getScoreMax() {
        return scoreMax.orNull();
    }

    /**
     * Set the max score
     *
     * @param scoreMax maximum score
     */
    public void setScoreMax(Float scoreMax) {
        this.scoreMax = Optional.fromNullable(scoreMax);
    }

    /**
     * Get if returns fields mapping
     *
     * @return if result should return fields mapping
     */
    @JsonProperty("return_fields_mapping")
    public Boolean getReturnFieldsMapping() {
        return returnFieldsMapping.orNull();
    }

    /**
     * Set if the query response will return field mappings (which ViSenze's
     * fields are mapped to which Client's fields).
     *
     * @param returnFieldsMapping Setting this to true will cause the response
     *                            to bepopulated with how ViSenze's fields are
     *                            mapped to Client's fields.
     */
    public void setReturnFieldsMapping(Boolean returnFieldsMapping) {
        this.returnFieldsMapping = Optional.fromNullable(returnFieldsMapping);
    }

    /**
     * Get if returns image s3 url
     *
     * @return if result should return s3 url for images
     */
    @JsonProperty("return_image_s3_url")
    public Boolean getReturnImageS3Url() {
        return returnImageS3Url.orNull();
    }

    /**
     * Set if returns image s3 url
     *
     * @param returnImageS3Url should return images s3 url
     */
    public void setReturnImageS3Url(Boolean returnImageS3Url) {
        this.returnImageS3Url = Optional.fromNullable(returnImageS3Url);
    }

    /**
     * Get sdk using the api
     *
     * @return which sdk is this
     */
    @JsonProperty("va_sdk")
    public String getVaSdk() {
        return vaSdk.orNull();
    }

    /**
     * Set sdk using the api
     *
     * @param vaSdk which sdk is this
     */
    public void setVaSdk(String vaSdk) {
        this.vaSdk = Optional.fromNullable(vaSdk);
    }

    /**
     * Get sdk version
     *
     * @return sdk version
     */
    @JsonProperty("va_sdk_version")
    public String getVaSdkVersion() {
        return vaSdkVersion.orNull();
    }

    /**
     * Set sdk version
     *
     * @param vaSdkVersion sdk version
     */
    public void setVaSdkVersion(String vaSdkVersion) {
        this.vaSdkVersion = Optional.fromNullable(vaSdkVersion);
    }

    /**
     * Get the custom mapping data
     *
     * @return custom_map
     */
    @JsonProperty("custom_map")
    public Map<String, String> getCustomParams() {
        return customParams.orNull();
    }

    /**
     * Set the custom mapping data
     *
     * @param customParams custom data
     */
    public void setCustomParams(Map<String, String> customParams) {
        this.customParams = Optional.fromNullable(customParams);
    }
}
