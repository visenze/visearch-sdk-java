package com.visenze.productsearch.param;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import static com.visenze.common.util.MultimapUtil.putIfPresent;
import static com.visenze.common.util.MultimapUtil.putList;
import static com.visenze.common.util.MultimapUtil.putMap;
import static com.visenze.common.util.MultimapUtil.setValueMap;
import static com.visenze.visearch.internal.constant.ViSearchHttpConstants.*;

import java.util.List;
import java.util.Map;

/**
 * <h1> Common Request Parameters </h1>
 * This class holds all the common parameters required by the different search
 * APIs that we support.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 08 Jan 2021
 */
public class BaseProductSearchParam {

    // The result page number. Default to 1. Max 50.
    protected Optional<Integer> page = Optional.absent();

    /**
     * The number of results returned per page
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

    protected Optional<Map<String, String>> filters = Optional.absent();

    protected Optional<Map<String, String>> textFilters = Optional.absent();

    protected Optional<List<String>> attrsToGet = Optional.absent();

    protected Optional<List<String>> facets = Optional.absent();

    protected Optional<Integer> facetsLimit = Optional.absent();

    protected Optional<Boolean> facetsShowCount = Optional.absent();

    protected Optional<String> sortBy = Optional.absent();

    protected Optional<String> groupBy = Optional.absent();

    protected Optional<Integer> groupLimit = Optional.absent();

    protected Optional<String> sortGroupBy = Optional.absent();

    protected Optional<String> sortGroupStrategy = Optional.absent();

    protected Optional<Boolean> score = Optional.absent();

    protected Optional<Float> scoreMin = Optional.absent();

    protected Optional<Float> scoreMax = Optional.absent();

    protected Optional<Float> colorRelWeight = Optional.absent();

    /**
     * Default to false, if set to true, will return catalog fields mappings
     */
    protected Optional<Boolean> returnFieldsMapping = Optional.absent();

    protected Optional<Boolean> returnQuerySysMeta = Optional.absent();

    protected Optional<Boolean> dedup = Optional.absent();

    protected Optional<Float> dedupThreshold = Optional.absent();

    /**
     * Return S3 URL (the copy) of the main product image
     */
    protected Optional<Boolean> returnImageS3Url = Optional.absent();

    protected Optional<Boolean> debug = Optional.absent();

    /**
     * Which SDK e.g. Javascript, Swift, Android, Java (for server side)
     */
    protected Optional<String> vaSdk = Optional.absent();

    protected Optional<String> vaSdkVersion = Optional.absent();

    /**
     * Custom mapping for future-proofing
     */
    protected Optional<Map<String, String>> customParams = Optional.absent();

    protected Optional<String> vaOs = Optional.absent();

    protected Optional<String> vaOsv = Optional.absent();

    protected Optional<String> vaDeviceBrand = Optional.absent();

    protected Optional<String> vaDeviceModel = Optional.absent();

    protected Optional<String> vaAppBundleId = Optional.absent();

    protected Optional<String> vaAppName = Optional.absent();

    protected Optional<String> vaAppVersion = Optional.absent();

    protected Optional<String> vaAaid = Optional.absent();

    protected Optional<String> vaDidmd5 = Optional.absent();

    protected Optional<Number> vaN1 = Optional.absent();

    protected Optional<Number> vaN2 = Optional.absent();

    protected Optional<String> vaS1 = Optional.absent();

    protected Optional<String> vaS2 = Optional.absent();

    protected Optional<Map<String, String>> vsConfig = Optional.absent();
    protected Optional<List<String>> vsAttrsToGet = Optional.absent();

    // allow override of default app key and placement ID
    private String appKey;
    private Integer placementId;


    public Multimap<String, String> toMultimap() {
        Multimap<String, String> multimap = HashMultimap.create();

        putIfPresent(multimap, page, PAGE);
        putIfPresent(multimap, limit, LIMIT);
        putIfPresent(multimap, facetsLimit, FACETS_LIMIT);
        putIfPresent(multimap, facetsShowCount, FACETS_SHOW_COUNT);
        putIfPresent(multimap, sortBy, SORT_BY);
        putIfPresent(multimap, groupBy, GROUP_BY);
        putIfPresent(multimap, groupLimit, GROUP_LIMIT);
        putIfPresent(multimap, sortGroupBy, SORT_GROUP_BY);
        putIfPresent(multimap, sortGroupStrategy, SORT_GROUP_STRATEGY);

        putIfPresent(multimap, score, SCORE);
        putIfPresent(multimap, scoreMin, SCORE_MIN);
        putIfPresent(multimap, scoreMax, SCORE_MAX);
        putIfPresent(multimap, colorRelWeight, COLOR_REL_WEIGHT);
        putIfPresent(multimap, returnFieldsMapping, RETURN_FIELDS_MAPPING);
        putIfPresent(multimap, returnQuerySysMeta, RETURN_QUERY_SYS_META);
        putIfPresent(multimap, dedup, DEDUP);
        putIfPresent(multimap, dedupThreshold, DEDUP_SCORE_THRESHOLD);
        putIfPresent(multimap, returnImageS3Url, RETURN_IMAGE_S3_URL);
        putIfPresent(multimap, debug, DEBUG);

        setAnalyticsParams(multimap);

        putList(multimap, attrsToGet, ATTRS_TO_GET);
        putList(multimap, vsAttrsToGet, VS_ATTRS_TO_GET);
        putList(multimap, facets, FACETS);

        setValueMap(multimap, filters, FILTERS);
        setValueMap(multimap, textFilters, TEXT_FILTERS);
        setValueMap(multimap, vsConfig, VS_CONFIG);

        putMap(multimap, customParams);

        return multimap;
    }

    private void setAnalyticsParams(Multimap<String, String> multimap) {
        putIfPresent(multimap, vaUid, VA_UID);
        putIfPresent(multimap, vaSid, VA_SID);
        putIfPresent(multimap, vaSdk, VA_SDK);
        putIfPresent(multimap, vaSdkVersion, VA_SDK_VERSION);
        putIfPresent(multimap, vaOs, VA_OS);
        putIfPresent(multimap, vaOsv, VA_OSV);
        putIfPresent(multimap, vaDeviceBrand, VA_DEVICE_BRAND);
        putIfPresent(multimap, vaDeviceModel, VA_DEVICE_MODEL);
        putIfPresent(multimap, vaAppBundleId, VA_APP_BUNDLE_ID);
        putIfPresent(multimap, vaAppName, VA_APP_NAME);
        putIfPresent(multimap, vaAppVersion, VA_APP_VERSION);
        putIfPresent(multimap, vaAaid, VA_AAID);
        putIfPresent(multimap, vaDidmd5, VA_DIDMD5);
        putIfPresent(multimap, vaN1, VA_N1);
        putIfPresent(multimap, vaN2, VA_N2);
        putIfPresent(multimap, vaS1, VA_S1);
        putIfPresent(multimap, vaS2, VA_S2);
    }


    /**
     * Get the result page number
     *
     * @return page number
     */
    public Integer getPage() { return page.orNull(); }

    /**
     * Set the result page number
     *
     * @param page page number to use
     */
    public void setPage(int page) {
        this.page = Optional.of(Math.max(page, 1));
    }

    /**
     * Get a page's limit
     *
     * @return Number of results per page
     */
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
    public Map<String, String> getFilters() {
        return filters.orNull();
    }

    /**
     * Set the map of filters that will help narrow search scope.
     * Conceptual and non-representational example:
     *      filters["category"] = "clothes";
     *      filters["another_filter_key"] = "some_other_filter_value";
     * Requires key to be in client's field name.
     *
     * @param filters A map of filters' key-value
     */
    public void setFilters(Map<String, String> filters) {
        this.filters = getOptionalMap(filters);
    }

    /**
     * Get the filters for text search
     *
     * @return filters for text search
     */
    public Map<String, String> getTextFilters() {
        return textFilters.orNull();
    }

    /**
     * Set the filters for text search. Requires key to be in client's field
     * name.
     *
     * @param textFilters filters for text search
     */
    public void setTextFilters(Map<String, String> textFilters) {
        this.textFilters = getOptionalMap(textFilters);
    }

    /**
     * Get the attributes required, common fields
     *      i.e. product_id, main_image_url, title
     *
     * @return list of attributes
     */
    public List<String> getAttrsToGet() {
        return attrsToGet.orNull();
    }

    /**
     * Set the attributes required, common fields
     *      i.e. product_id, main_image_url, title
     * Requires to be in client's field name.
     *
     * @param attrsToGet list of attributes
     */
    public void setAttrsToGet(List<String> attrsToGet) {
        this.attrsToGet = getOptionalList(attrsToGet);
    }

    public List<String> getVsAttrsToGet() {
        return vsAttrsToGet.orNull();
    }

    public void setVsAttrsToGet(List<String> vsAttrsToGet) {
        this.vsAttrsToGet = getOptionalList(vsAttrsToGet);
    }

    /**
     * Get the facets
     *
     * @return list of facets
     */
    public List<String> getFacets() {
        return facets.orNull();
    }

    /**
     * Set the facets
     *
     * @param facets list of facets
     */
    public void setFacets(List<String> facets) {
        this.facets = getOptionalList(facets);
    }

    /**
     * Get the facets' limit
     *
     * @return facets' limit
     */
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
    public String getSortBy() {
        return sortBy.orNull();
    }

    /**
     * Set the 'sort by' criteria. Requires to be in client's field name.
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
    public String getGroupBy() {
        return groupBy.orNull();
    }

    /**
     * Set the 'group by' criteria. Requires to be in client's field name.
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
    public Float getScoreMax() {
        return scoreMax.orNull();
    }

    /**
     * Set the max score
     *
     * @param scoreMax maximum score
     */
    public void setScoreMax(Float scoreMax) { this.scoreMax = Optional.fromNullable(scoreMax); }

    /**
     * Get colorRelWeight
     *
     * @return colorRelWeight
     */
    public Float getColorRelWeight() { return colorRelWeight.orNull(); }

    /**
     * Set the coloRelWeight
     *
     * @param weight 0 to disable color relevance
     */
    public void setColorRelWeight(Float weight) { this.colorRelWeight = Optional.fromNullable(weight); }

    /**
     * Get if returns fields mapping
     *
     * @return if result should return fields mapping
     */
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
     * Get if result should return system metadata for query images
     *
     * @return Boolean
     */
    public Boolean getReturnQuerySysMeta() {
        return returnQuerySysMeta.orNull();
    }

    /**
     * Set if result should return system metadata for query images
     *
     * @param returnQuerySysMeta if set to true, system will return metadata
     */
    public void setReturnQuerySysMeta(Boolean returnQuerySysMeta) {
        this.returnQuerySysMeta = Optional.fromNullable(returnQuerySysMeta);
    }

    /**
     * Get dedup
     *
     * @return dedup
     */
    public Boolean getDedup() { return dedup.orNull(); }

    /**
     * Set dedup
     *
     * @param b If dedup should be used
     */
    public void setDedup(Boolean b) { this.dedup = Optional.fromNullable(b); }

    /**
     * Get dedupThreshold
     *
     * @return dedupThreshold
     */
    public Float getDedupThreshold() { return dedupThreshold.orNull(); }

    /**
     * Set dedupThreshold
     *
     * @param thresh dedup threshold value
     */
    public void setDedupThreshold(Float thresh) { this.dedupThreshold = Optional.fromNullable(thresh); }

    /**
     * Get debug
     *
     * @return Boolean
     */
    public Boolean getDebug() { return debug.orNull(); }

    /**
     * Set debug
     *
     * @param debug Set true to see debug information
     */
    public void setDebug(Boolean debug) { this.debug = Optional.fromNullable(debug); }

    /**
     * Get sdk using the api
     *
     * @return which sdk is this
     */
    public String getVaSdk() { return vaSdk.orNull(); }

    /**
     * Set sdk using the api
     *
     * @param vaSdk which sdk is this
     */
    public void setVaSdk(String vaSdk) { this.vaSdk = Optional.fromNullable(vaSdk); }

    /**
     * Get sdk version
     *
     * @return sdk version
     */
    public String getVaSdkVersion() { return vaSdkVersion.orNull(); }

    /**
     * Set sdk version
     *
     * @param vaSdkVersion sdk version
     */
    public void setVaSdkVersion(String vaSdkVersion) { this.vaSdkVersion = Optional.fromNullable(vaSdkVersion); }

    public Map<String, String> getCustomParams() {
        return customParams.orNull();
    }

    public void setCustomParams(Map<String, String> customParams) {
        this.customParams = Optional.fromNullable(customParams);
    }

    public Map<String, String> getVsConfig() {
        return vsConfig.orNull();
    }

    public void setVsConfig(Map<String, String> vsConfig) {
        this.vsConfig = Optional.fromNullable(vsConfig);
    }

    private Optional<Map<String, String>> getOptionalMap(Map<String, String> map){
        if (map == null || map.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(map);
    }

    private Optional<List<String>> getOptionalList(List<String> list){
        if (list == null || list.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(list);
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getPlacementId() {
        return placementId;
    }

    public void setPlacementId(Integer placementId) {
        this.placementId = placementId;
    }
}
