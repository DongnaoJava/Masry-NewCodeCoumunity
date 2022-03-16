package com.bin.util;

public class RedisKeyUtil {
    private final static String SPLIT = ":";
    private final static String PREFIX_ENTITY_LIKE = "like:entity";
    private final static String PREFIX_USER_LIKE = "like:user";
    private final static String PREFIX_FOLLOWER = "follower:user";
    private final static String PREFIX_FOLLOWEE = "followee:user";

    //帖子评论获得的赞
    //like:entity:entityType:entityId
    public static String getRedisLikeKey(Integer entityType, Integer entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //like:user:userId  某个用户获得的赞
    public static String getRedisUserLikeKey(Integer userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //follower:user:entityType:entityId 某个实体被关注的用户
    public static String getRedisFollowerKey(Integer entityType, Integer entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //follower:user:userId 某个用户关注的实体
    public static String getRedisFolloweeKey(Integer userId, Integer entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }
}
