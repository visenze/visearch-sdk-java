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
public class BaseSearchParam {

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
    protected Optional<String> va_uid = Optional.absent();

    /**
     * Analytics session ID
     */
    protected Optional<String> va_sid = Optional.absent();

    /**
     * For first release, can consider to narrow scope to only product_id,
     * price, category, brand, original_price
     */
    protected Optional<Map<String, String>> filters = Optional.absent();

    /**
     * For fq for text search
     */
    protected Optional<Map<String, String>> text_filters = Optional.absent();

    /**
     * By default we should always show the common fields (title, product_id,
     * main_image_url, title, price, product_url, brand)
     */
    protected Optional<List<String>> attrs_to_get = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<List<String>> facets = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Integer> facets_limit = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Boolean> facets_show_count = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> sort_by = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> group_by = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Integer> group_limit = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> sort_group_by = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<String> sort_group_strategy = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Boolean> score = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Float> score_min = Optional.absent();

    /**
     * Same as Visearch
     */
    protected Optional<Float> score_max = Optional.absent();

    /**
     * Default to false, if set to true, will return catalog fields mappings
     */
    protected Optional<Boolean> return_fields_mapping = Optional.absent();

    /**
     * Return S3 URL (the copy) of the main product image
     */
    protected Optional<Boolean> return_image_s3_url = Optional.absent();

    /**
     * Set to true to see debug information
     */
    protected Optional<Boolean> debug = Optional.absent();

    /**
     * Set to visenze_admin to exclude from billing
     */
    protected Optional<String> vtt_source = Optional.absent();

    /**
     * Which SDK e.g. Javascript, Swift, Android, Java (for server side)
     */
    protected Optional<String> va_sdk = Optional.absent();

    /**
     * Store our tracking SDK’s version e.g. “1.0.2”. For debugging if there is
     * a bug and also to measure SDK popularity/usage
     */
    protected Optional<String> va_sdk_version = Optional.absent();

    /**
     * Custom mapping for future-proofing
     */
    protected Optional<Map<String, String>>  custom_map = Optional.absent();

    /**
     * Traffic source (for placement’s concept). This will store the placement
     * ID instead of placement name
     */
    protected Integer placement_id;

    /**
     * This should be temporary? Do we store app_key as query or inside the
     * authentication header as credentials.
     */
    protected String app_key;



    //va_os
    //va_osv
    //va_device_brand
    //va_device_model
    //va_app_bundle_id
    //va_app_name
    //va_app_version
    //va_aaid
    //va_didmd5
    //va_n1
    //va_n2
    //va_s1
    //va_s2

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

        if (va_uid.isPresent())
            multimap.put(VA_UID, va_uid.get());

        if (va_sid.isPresent())
            multimap.put(VA_SID, va_sid.get());

        if (filters.isPresent()) {
            for (Map.Entry<String, String> entry : filters.get().entrySet())
                multimap.put(FILTERS, entry.getKey() + COLON + entry.getValue());
        }

        if (text_filters.isPresent()) {
            for (Map.Entry<String, String> entry : text_filters.get().entrySet())
                multimap.put(TEXT_FILTERS, entry.getKey() + COLON + entry.getValue());
        }

        if (attrs_to_get.isPresent()) {
            for (String val : attrs_to_get.get())
                multimap.put(ATTR_TO_GET, val);
        }

        if (facets.isPresent()) {
            for (String val : facets.get())
                multimap.put(FACETS, val);
        }

        if (facets_limit.isPresent())
            multimap.put(FACETS_LIMIT, facets_limit.get().toString());

        if (facets_show_count.isPresent())
            multimap.put(FACETS_SHOW_COUNT, facets_show_count.get().toString());

        if (sort_by.isPresent())
            multimap.put(SORT_BY, sort_by.get());

        if (group_by.isPresent())
            multimap.put(GROUP_BY, group_by.get());

        if (group_limit.isPresent())
            multimap.put(GROUP_LIMIT, group_limit.get().toString());

        if (sort_group_by.isPresent())
            multimap.put(SORT_GROUP_BY, sort_group_by.get());

        if (sort_group_strategy.isPresent())
            multimap.put(SORT_GROUP_STRATEGY, sort_group_strategy.get());

        if (score.isPresent())
            multimap.put(SCORE, score.get().toString());

        if (score_min.isPresent())
            multimap.put(SCORE_MIN, score_min.get().toString());

        if (score_max.isPresent())
            multimap.put(SCORE_MAX, score_max.get().toString());

        if (return_fields_mapping.isPresent())
            multimap.put(RETURN_FIELDS_MAPPING, return_fields_mapping.get().toString());

        if (return_image_s3_url.isPresent())
            multimap.put(RETURN_IMAGE_S3_URL, return_image_s3_url.get().toString());

        if (debug.isPresent())
            multimap.put(DEBUG, debug.get().toString());

        if (vtt_source.isPresent())
            multimap.put(VTT_SOURCE, vtt_source.get());

        if (va_sdk.isPresent())
            multimap.put(VA_SDK, va_sdk.get());

        if (va_sdk_version.isPresent())
            multimap.put(VA_SDK_VERSION, va_sdk_version.get());

        multimap.put(PLACEMENT_ID, placement_id.toString());

        multimap.put(APP_KEY, app_key);

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
     * @return va_uid
     */
    @JsonProperty("va_uid")
    public String getVaUid() {
        return va_uid.orNull();
    }

    /**
     * Set the unique identifier for client application/usage
     *
     * @param va_uid uid to identify this app
     */
    public void setVaUid(String va_uid) {
        // dont allow empty string
        this.va_uid = Optional.fromNullable(va_uid.isEmpty() ? null : va_uid);
    }

    /**
     * Get the session ID for analytics
     *
     * @return va_sid
     */
    @JsonProperty("va_sid")
    public String getVaSid() {
        return va_sid.orNull();
    }

    /**
     * Set the session ID for analytics
     *
     * @param va_sid ID to identify this 'session' for analytics
     */
    public void setVaSid(String va_sid) {
        // dont allow empty string
        this.va_sid = Optional.fromNullable(va_sid.isEmpty() ? null : va_sid);
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
        if (filters.isEmpty()) {
            this.filters = Optional.absent();
        }
        // remove null entries
        else {
            while (filters.values().remove(null));
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
        return text_filters.orNull();
    }

    /**
     * Set the filters for text search
     *
     * @param text_filters filters for text search
     */
    public void setTextFilters(Map<String, String> text_filters) {
        // dont allow empty map
        if (text_filters.isEmpty()) {
            this.text_filters = Optional.absent();
        }
        // remove null entries
        else {
            while (text_filters.values().remove(null));
            this.text_filters = Optional.of(text_filters);
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
        return attrs_to_get.orNull();
    }

    /**
     * Set the attributes required, common fields
     *      i.e. product_id, main_image_url, title
     *
     * @param attrs_to_get list of attributes
     */
    public void setAttrsToGet(List<String> attrs_to_get) {
        // dont allow empty list
        if (attrs_to_get.isEmpty()) {
            this.attrs_to_get = Optional.absent();
        }
        else {
            this.attrs_to_get = Optional.of(attrs_to_get);
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
        return facets_limit.orNull();
    }

    /**
     * Set the facets' limit
     *
     * @param facets_limit facets' limit
     */
    public void setFacetsLimit(Integer facets_limit) {
        this.facets_limit = Optional.fromNullable(facets_limit);
    }

    /**
     * Get the facets show count
     *
     * @return should facet show count
     */
    @JsonProperty("facets_show_count")
    public Boolean getFacetsShowCount() {
        return facets_show_count.orNull();
    }

    /**
     * Set the facets show count
     *
     * @param facets_show_count should facet show count
     */
    public void setFacetsShowCount(Boolean facets_show_count) {
        this.facets_show_count = Optional.fromNullable(facets_show_count);
    }

    /**
     * Get the 'sort by' string
     *
     * @return criteria of 'sort by'
     */
    @JsonProperty("sort_by")
    public String getSortBy() {
        return sort_by.orNull();
    }

    /**
     * Set the 'sort by' criteria
     *
     * @param sort_by criteria of 'sort by'
     */
    public void setSortBy(String sort_by) {
        this.sort_by = Optional.fromNullable(sort_by);
    }

    /**
     * Get the 'group by' criteria
     *
     * @return criteria of 'group by'
     */
    @JsonProperty("group_by")
    public String getGroupBy() {
        return group_by.orNull();
    }

    /**
     * Set the 'group by' criteria
     *
     * @param group_by criteria of 'group by'
     */
    public void setGroupBy(String group_by) {
        this.group_by = Optional.fromNullable(group_by);
    }

    /**
     * Get the group limit
     *
     * @return group limit
     */
    @JsonProperty("group_limit")
    public Integer getGroupLimit() {
        return group_limit.orNull();
    }

    /**
     * Set the group limit
     *
     * @param group_limit group limit
     */
    public void setGroupLimit(Integer group_limit) {
        this.group_limit = Optional.fromNullable(group_limit);
    }

    /**
     * Get the 'sort group by'
     *
     * @return criteria for 'sort group by'
     */
    @JsonProperty("sort_group_by")
    public String getSortGroupBy() {
        return sort_group_by.orNull();
    }

    /**
     * Set the 'sort group by'
     *
     * @param sort_group_by criteria for 'sort group by'
     */
    public void setSortGroupBy(String sort_group_by) {
        this.sort_group_by = Optional.fromNullable(sort_group_by);
    }

    /**
     * Get the 'sort group by' strategy
     *
     * @return strategy
     */
    @JsonProperty("sort_group_strategy")
    public String getSortGroupStrategy() {
        return sort_group_strategy.orNull();
    }

    /**
     * Set the 'sort group by' strategy
     *
     * @param sort_group_strategy sort strategy
     */
    public void setSortGroupStrategy(String sort_group_strategy) {
        this.sort_group_strategy = Optional.fromNullable(sort_group_strategy);
    }

    /**
     * Get score
     *
     * @return score
     */
    @JsonProperty("score")
    public Boolean getScore() {
        return score.orNull();
    }

    /**
     * Set score
     *
     * @param score score
     */
    public void setScore(Boolean score) {
        this.score = Optional.fromNullable(score);
    }

    /**
     * Get the min score
     *
     * @return minimum score
     */
    @JsonProperty("score_min")
    public Float getScoreMin() {
        return score_min.orNull();
    }

    /**
     * Set the min score
     *
     * @param score_min minimum score
     */
    public void setScoreMin(Float score_min) {
        this.score_min = Optional.fromNullable(score_min);
    }

    /**
     * Get the max score
     *
     * @return maximum score
     */
    @JsonProperty("score_max")
    public Float getScoreMax() {
        return score_max.orNull();
    }

    /**
     * Set the max score
     *
     * @param score_max maximum score
     */
    public void setScoreMax(Float score_max) {
        this.score_max = Optional.fromNullable(score_max);
    }

    /**
     * Get if returns fields mapping
     *
     * @return if result should return fields mapping
     */
    @JsonProperty("return_fields_mapping")
    public Boolean getReturnFieldsMapping() {
        return return_fields_mapping.orNull();
    }

    /**
     * Set if the query response will return field mappings (which ViSenze's
     * fields are mapped to which Client's fields).
     *
     * @param return_fields_mapping Setting this to true will cause the response
     *                              to be populated with how ViSenze's fields
     *                              are mapped to Client's fields.
     */
    public void setReturnFieldsMapping(Boolean return_fields_mapping) {
        this.return_fields_mapping = Optional.fromNullable(return_fields_mapping);
    }

    /**
     * Get if returns image s3 url
     *
     * @return if result should return s3 url for images
     */
    @JsonProperty("return_image_s3_url")
    public Boolean getReturnImageS3Url() {
        return return_image_s3_url.orNull();
    }

    /**
     * Set if returns image s3 url
     *
     * @param return_image_s3_url should return images s3 url
     */
    public void setReturnImageS3Url(Boolean return_image_s3_url) {
        this.return_image_s3_url = Optional.fromNullable(return_image_s3_url);
    }

    /**
     * Get debug required
     *
     * @return if result should include debug info
     */
    @JsonProperty("debug")
    public Boolean getDebug() {
        return debug.orNull();
    }

    /**
     * Set debug required
     *
     * @param debug if result should include debug info
     */
    public void setDebug(Boolean debug) {
        this.debug = Optional.fromNullable(debug);
    }

    /**
     * Get source for admin usage to exclude billing
     *
     * @return 'visenze_admin' or not
     */
    @JsonProperty("vtt_source")
    public String getVttSource() {
        return vtt_source.orNull();
    }

    /**
     * Set source for admin usage to exclude billing
     *
     * @param vtt_source 'visenze_admin'?
     */
    public void setVttSource(String vtt_source) {
        this.vtt_source = Optional.fromNullable(vtt_source);
    }

    /**
     * Get sdk using the api
     *
     * @return which sdk is this
     */
    @JsonProperty("va_sdk")
    public String getVaSdk() {
        return va_sdk.orNull();
    }

    /**
     * Set sdk using the api
     *
     * @param va_sdk which sdk is this
     */
    public void setVaSdk(String va_sdk) {
        this.va_sdk = Optional.fromNullable(va_sdk);
    }

    /**
     * Get sdk version
     *
     * @return sdk version
     */
    @JsonProperty("va_sdk_version")
    public String getVaSdkVersion() {
        return va_sdk_version.orNull();
    }

    /**
     * Set sdk version
     *
     * @param va_sdk_version sdk version
     */
    public void setVaSdkVersion(String va_sdk_version) {
        this.va_sdk_version = Optional.fromNullable(va_sdk_version);
    }

    /**
     * Get the placement id
     *
     * @return placement_id
     */
    @JsonProperty("placement_id")
    public Integer getPlacementId() {
        return placement_id;
    }

    /**
     * Set the placement id
     *
     * @param placement_id placement id
     */
    public void setPlacementId(Integer placement_id) {
        this.placement_id = placement_id;
    }

    /**
     * Get the app_key
     *
     * @return app_key
     */
    @JsonProperty("app_key")
    public String getAppKey() {
        return app_key;
    }

    /**
     * Set the app_key
     *
     * @param app_key app_key
     */
    public void setAppKey(String app_key) { this.app_key = app_key; }

    /**
     * Get the custom mapping data
     *
     * @return custom_map
     */
    @JsonProperty("custom_map")
    public Map<String, String> getCustomMap() {
        return custom_map.orNull();
    }

    /**
     * Set the custom mapping data
     *
     * @param custom_map custom data
     */
    public void setCustomMap(Map<String, String> custom_map) {
        this.custom_map = Optional.fromNullable(custom_map);
    }
}
