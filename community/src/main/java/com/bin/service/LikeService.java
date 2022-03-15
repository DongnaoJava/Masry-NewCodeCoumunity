package com.bin.service;

public interface LikeService {
    void like(Integer userId, Integer entityType, Integer entityId);
    long findLikeCount(Integer entityType, Integer entityId);
    Integer isLike(Integer userId,Integer entityType, Integer entityId);
}
