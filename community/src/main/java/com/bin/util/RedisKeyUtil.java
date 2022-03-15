package com.bin.util;

public class RedisKeyUtil {
    private final static String SPLIT = ":";
    private final static String PREFIX_ENTITY_LIKE = "like:entity";

    //like:entity:entityType:entityId
    public static String getRedisLikeKey(Integer entityType, Integer entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
