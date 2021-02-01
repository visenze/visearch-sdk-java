package com.visenze.common.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <h1> ViJsonAny - A JsonNode wrapper </h1>
 * This class is created to handle multiple typed JSON objects that do not have
 * nested/children json tree nodes. "Multiple typed JSON objects" are those that
 * hope to be deserialized as:
 *      Map (String, String) OR List (String) OR Map (String, String)
 *
 * The statement above is only possible in Java if it is denoted as:
 *      Map ( String, Object), or with Json support, Map(String, JsonNode)
 *
 * The initial intention is to be a replacement for Map(String, JsonNode):
 *      Map(String, ViJsonAny)
 *
 * With this wrapper class, you could check if it is a definite value or an
 * array before extracting it as that type. The same functionality can be
 * achieved by using JsonNode directly but this class hides the boiler plate
 * code that you would otherwise need to type.
 * <p>
 * It does not support recursively identifying and extracting json nodes' data.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 20 Jan 2021
 */
public class ViJsonAny extends ViJsonMapper{
    /**
     * The raw json node data.
     */
    private final JsonNode jsonNode;

    /**
     * Valid if this object represents a non-container type
     */
    private Optional<Object> data = Optional.absent();

    private Class<?> dataType;
    private Class<?> containerType;

    /**
     * Constructor, annotated with @JsonCreator for JSON to deserialize into
     * this constructor.
     *
     * @param jsonNode The node to wrap.
     */
    @JsonCreator
    public ViJsonAny(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
        this.dataType = null;
        this.containerType = null;
    }

    /**
     * Constructor allows json formatted strings
     *
     * @param jsonString JSON formatted string
     */
    public ViJsonAny(String jsonString) {
        try {
            this.jsonNode = mapper.readTree(jsonString);
        } catch(IOException e) {
            throw new InternalViSearchException(e.getMessage());
        }
        this.dataType = null;
        this.containerType = null;
    }

    /**
     * Get the original JsonNode.
     *
     * @return jsonNode
     */
    public JsonNode getJsonNode() { return jsonNode; }

    /**
     * Get the node as String value.
     *
     * @return String, else null if value cannot be converted from raw to String
     * or value was already converted from raw to something else.
     */
    public String asString() {
        return tryGetValueAs(new TypeReference<String>() {}, String.class);
    }

    /**
     * Get the node as Integer value.
     *
     * @return Integer, else null if value cannot be converted from raw to
     * Integer or value was already converted from raw to something else.
     */
    public Integer asInteger() {
        return tryGetValueAs(new TypeReference<Integer>() {}, Integer.class);
    }

    /**
     * Get the node as Float value.
     *
     * @return Float, else null if value cannot be converted from raw to Float
     * or value was already converted from raw to something else.
     */
    public Float asFloat() {
        return tryGetValueAs(new TypeReference<Float>() {}, Float.class);
    }

    /**
     * Get the node as Double value.
     *
     * @return Double, else null if value cannot be converted from raw to Double
     * or value was already converted from raw to something else.
     */
    public Double asDouble() {
        return tryGetValueAs(new TypeReference<Double>() {}, Double.class);
    }

    /**
     * Get the node as List of String value.
     *
     * @return List (String), else null if value cannot be converted from raw to
     * List String or value was already converted from raw to something else.
     */
    public List<String> asStringList() {
        return tryGetListAs(new TypeReference<List<String>>() {}, String.class);
    }

    /**
     * Get the node as List of Integer value.
     *
     * @return List of Integer, else null if value cannot be converted from raw to
     * List of Integer or value was already converted from raw to something else.
     */
    public List<Integer> asIntegerList() {
        return tryGetListAs(new TypeReference<List<Integer>>() {}, Integer.class);
    }

    /**
     * Get the node as List of Float value.
     *
     * @return Float list, else null if value cannot be converted from raw to
     * float list or value was already converted from raw to something else.
     */
    public List<Float> asFloatList() {
        return tryGetListAs(new TypeReference<List<Float>>() {}, Float.class);
    }

    /**
     * Get the node as List of Double value.
     *
     * @return List of Double, else null if value cannot be converted from raw to
     * Double list or value was already converted from raw to something else.
     */
    public List<Double> asDoubleList() {
        return tryGetListAs(new TypeReference<List<Double>>() {}, Double.class);
    }

    /**
     * Get the node as Map (String,String)  value.
     *
     * @return Map  , else null if value cannot be converted from
     * raw to Map or value was already converted from raw to
     * something else.
     */
    public Map<String,String> asStringStringMap() {
        return tryGetMapAs(new TypeReference<Map<String,String>>() {}, String.class);
    }

    /**
     * Get the node as Map (String, Integer)  value.
     *
     * @return Map (String,Integer) , else null if value cannot be converted from
     * raw to Map (String,Integer) or value was already converted from raw to
     * something else.
     */
    public Map<String,Integer> asStringIntegerMap() {
        return tryGetMapAs(new TypeReference<Map<String,Integer>>() {}, Integer.class);
    }

    /**
     * Attempt to retrieve the raw data as data of type referenced. If the raw
     * data is being converted for the first time, it will store the dataType
     * for future reference to check if the retrieval is correct.
     *
     * @param type Type reference of how it wants to be retrieved
     * @param dataType Data type clazz to validate with
     *
     * @return Null if failed to convert.
     */
    @SuppressWarnings("unchecked")
    private <T> T tryGetValueAs(TypeReference<T> type, Class<T> dataType) {
        if (!isLoaded()) {
            T validValue = loadAsValue(type);
            if (validValue == null)
                return null;
            this.data = Optional.of((Object)validValue);
            this.dataType = dataType;
        }
        Object data = this.data.orNull();
        if (data == null || this.dataType != dataType)
            return null;
        return (T)data;
    }

    /**
     * Attempt to retrieve the raw data as data of type referenced. If the raw
     * data is being converted for the first time, it will store the dataType
     * for future reference to check if the retrieval is correct. List edition.
     *
     * @param type Type reference of how it wants to be retrieved
     * @param dataType Data type clazz to validate with
     *
     * @return Null if failed to convert.
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> tryGetListAs(TypeReference<List<T>> type, Class<T> dataType) {
        if (!isLoaded()) {
            List<T> validValues = loadAsArray(type);
            if (validValues == null)
                return null;
            this.data = Optional.of((Object)validValues);
            this.dataType = dataType;
            this.containerType = List.class;
        }
        Object data = this.data.orNull();
        if (data == null || this.dataType != dataType || this.containerType != List.class)
            return null;
        return (List<T>)data;
    }

    /**
     * Attempt to retrieve the raw data as data of type referenced. If the raw
     * data is being converted for the first time, it will store the dataType
     * for future reference to check if the retrieval is correct. Map edition.
     *
     * @param type Type reference of how it wants to be retrieved
     * @param dataType Data type clazz to validate with
     *
     * @return Null if failed to convert.
     */
    @SuppressWarnings("unchecked")
    private <V> Map<String,V> tryGetMapAs(TypeReference<Map<String,V>> type, Class<V> dataType) {
        if (!isLoaded()) {
            Map<String,V> validValuePairs = loadAsMap(type);
            if (validValuePairs == null)
                return null;
            this.data = Optional.of((Object)validValuePairs);
            this.dataType = dataType;
            this.containerType = Map.class;
        }
        Object data = this.data.orNull();
        if (data == null || this.dataType != dataType || this.containerType != Map.class)
            return null;
        return (Map<String,V>)data;
    }

    /**
     * Check if the data was already loaded.
     *
     * @return True if dataType was already set by a tryGet... method.
     */
    private Boolean isLoaded() {
        return this.dataType != null;
    }

    /**
     * Get Object mapper to load as List
     *
     * @return List if its valid
     */
    private <T> List<T> loadAsArray(TypeReference<? extends List<T>> type) {
        // validate node type
        if (!this.jsonNode.isArray()) {
            return null;
        }
        // try read as type
        try {
            return mapper.readValue(jsonNode.toString(), type);
        } catch(IOException e) {
            throw new InternalViSearchException(e.getMessage());
        }
    }

    /**
     * Get Object mapper to load as Map
     *
     * @return Map if its valid
     */
    private <K,V> Map<K,V> loadAsMap(TypeReference<? extends Map<K,V>> type) {
        // validate node type
        if (!this.jsonNode.isContainerNode() || this.jsonNode.isArray()) {
            return null;
        }
        // try read as type
        try {
            return mapper.readValue(jsonNode.toString(), type);
        } catch(IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_EXCEPTION, e.getMessage());
        }
    }

    /**
     * Get Object mapper to load as T
     *
     * @return T if its valid
     */
    private <T> T loadAsValue(TypeReference<T> type) {
        // validate node type
        if (!this.jsonNode.isValueNode()) {
            return null;
        }
        // try read as type
        try {
            return mapper.readValue(jsonNode.toString(), type);
        } catch(IOException e) {
            throw new InternalViSearchException(e.getMessage());
        }
    }

}
