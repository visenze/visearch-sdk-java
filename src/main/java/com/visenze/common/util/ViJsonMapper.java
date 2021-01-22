package com.visenze.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <h1> ViJsonMapper </h1>
 * Extend this class to use the ObjectMapper. We keep a single instance in the
 * abstract class and all derived can share this instance to reduce duplicates
 * since it is an expensive resource.
 *
 * @author Shannon Tan
 * @version 1.0
 * @since 22 Jan 2021
 */
public abstract class ViJsonMapper {
    protected static final ObjectMapper mapper = new ObjectMapper();
}
