package com.visenze.visearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.visearch.internal.constant.ViSearchHttpConstants;

import java.util.List;
import java.util.Map;

/**
 * Created by dejun on 5/6/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectSearchResult {
    private String type;
    private Float score;
    private Float rerankScore;
    private List<Integer> box;

    @JsonProperty("box_type")
    private String boxType;

    private Map<String, List<String>> attributes;
    private Map<String, List<String>> attributesList;
    private int total;
    private List<ImageResult> result;
    private List<Facet> facets;
    private List<GroupSearchResult> groupResults;
    private List<Integer> point;

    // API-9128 add new STI fields
    private String id;
    private String category;
    private String name;

    @JsonProperty("excluded_im_names")
    private List<String> excludedImNames;

    public ObjectSearchResult() {

    }

    public String getType() {
        return type;
    }

    public Float getScore() {
        return score;
    }

    public List<Integer> getBox() {
        return box;
    }

    public String getBoxType() {
        return boxType;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public Map<String, List<String>> getAttributesList() {
        return attributesList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ImageResult> getResult() {
        return result;
    }

    public void setResult(List<ImageResult> result) {
        this.result = result;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public void setBox(List<Integer> box) {
        this.box = box;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public void setAttributesList(Map<String, List<String>> attributesList) {
        this.attributesList = attributesList;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public List<GroupSearchResult> getGroupResults() {
        return groupResults;
    }

    @JsonProperty(ViSearchHttpConstants.GROUP_RESULTS)
    public void setGroupResults(List<GroupSearchResult> groupResults) {
        this.groupResults = groupResults;
    }

    public List<Integer> getPoint() {
        return point;
    }

    public void setPoint(List<Integer> point) {
        this.point = point;
    }

    public Float getRerankScore() {
        return rerankScore;
    }

    public void setRerankScore(Float rerankScore) {
        this.rerankScore = rerankScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getExcludedImNames() {
        return excludedImNames;
    }

    public void setExcludedImNames(List<String> excludedImNames) {
        this.excludedImNames = excludedImNames;
    }
}
