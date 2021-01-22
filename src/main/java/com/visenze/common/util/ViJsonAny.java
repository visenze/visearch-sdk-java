package com.visenze.common.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
 *      Map<String, String OR List<String> OR Map<String, String>>
 *
 * The statement above is only possible in Java if it is denoted as:
 *      Map<String, Object>, or with Json support, Map<String, JsonNode>
 *
 * The initial intention is to be a replacement for Map<String, JsonNode>:
 *      Map<String, ViJsonAny>
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
    private JsonNode jsonNode;

    /**
     * Constructor, annotated with @JsonCreator for JSON to deserialize into
     * this constructor.
     *
     * @param jsonNode The node to wrap.
     */
    @JsonCreator
    public ViJsonAny(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
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
    }

    /**
     * Get the original JsonNode.
     *
     * @return jsonNode
     */
    public JsonNode getJsonNode() { return jsonNode; }

    /**
     * Check if this json node is an illegal/unsupported case (nested child).
     *
     * @return If this node is illegal
     */
    public Boolean isIllegal() {
        if (jsonNode.isContainerNode()) {
            for (JsonNode n : jsonNode) {
                if (n.isContainerNode())
                    return true;
            }
        }
        return false;
    }

    /**
     * Check if this json node is an array.
     *
     * @return If this node is an array
     */
    public Boolean isArray() {
        return jsonNode.isArray();
    }

    /**
     * Check if this json node is the definite value (integer, boolean, etc) and
     * not a container (list, map, etc) - 'leaf' node.
     *
     * @return If this node is a 'leaf' node
     */
    public Boolean isValue() {
        return jsonNode.isValueNode();
    }

    /**
     * Retrieve this node as a list of objects.
     *
     * @param <T> The return type of the list, usually String
     * @return A list of object
     */
    public <T> List<T> getAsList(TypeReference<List<T>> type) {
        try {
            return mapper.readValue(jsonNode.toString(), type);
        } catch(IOException e) {
            throw new InternalViSearchException(e.getMessage());
        }
    }


    /**
     * Retrieve this node as a definite value - 'leaf' node.
     *
     * @param <T> The return type of the object
     * @return An object that represents the actual value
     */
    public <T> T getAsValue(TypeReference<T> type) {
        try {
            return mapper.readValue(jsonNode.toString(), type);
        } catch(IOException e) {
            throw new InternalViSearchException(e.getMessage());
        }
    }

    /**
     * Retrieve this node as a Map container.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return The Map<Key, Value> of the object, usually Map<String, String>
     */
    public <K,V> Map<K,V> getAsMap(TypeReference<Map<K,V>> type) {
        try {
            return mapper.readValue(jsonNode.toString(), type);
        } catch(IOException e) {
            throw new InternalViSearchException(ResponseMessages.PARSE_EXCEPTION, e.getMessage());
        }
    }


}
