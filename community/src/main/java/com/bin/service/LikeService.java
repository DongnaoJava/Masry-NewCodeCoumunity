package com.bin.service;

public interface LikeService {
    void like(Integer userId, Integer entityType, Integer entityId,Integer discussPostId);
    long findLikeCount(Integer entityType, Integer entityId);
    Integer isLike(Integer userId,Integer entityType, Integer entityId);
    void incrUserGetLikeCount(Integer visitorId,Integer userId,Integer entityType, Integer entityId);
    void decrUserGetLikeCount(Integer visitorId,Integer userId,Integer entityType, Integer entityId);
    Long findUserGetLikeCount(Integer userId);
}
