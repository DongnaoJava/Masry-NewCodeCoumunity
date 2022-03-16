package com.bin.service;

import java.util.List;
import java.util.Map;

public interface FollowService {
    void follow(Integer userId, Integer entityType, Integer entityId);
    void cancelFollow(Integer userId, Integer entityType, Integer entityId);
    Long getFollowerCount(Integer entityType, Integer entityId);
    Long getFolloweeCount(Integer userId,Integer entityType);
    Integer getStatusOfFollow(Integer userId, Integer entityType, Integer entityId);
    List<Map<String, Object>> getFollowee(Integer userId, Integer entityType,Integer offset,Integer limit);
}
