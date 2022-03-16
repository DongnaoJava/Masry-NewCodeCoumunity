package com.bin.service.impl;

import com.bin.bean.CommunityConstant;
import com.bin.service.LikeService;
import com.bin.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LikeServiceImpl implements LikeService, CommunityConstant {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private DiscussPostServiceImpl discussPostService;

    //点赞
    @Override
    public void like(Integer userId, Integer entityType, Integer entityId) {
        //entityType 1是帖子，2是评论
        String redisLikeKey = RedisKeyUtil.getRedisLikeKey(entityType, entityId);
        BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(redisLikeKey);
        Boolean isMember = operations.isMember(userId);
        //寻找点赞的帖子或者评论的发布者
        Integer visitorId = entityType == ENTITY_TYPE_POST ? discussPostService.selectDiscussPostById(entityId).getUserId() : commentService.selectCommentById(entityId).getUserId();
        if (isMember) {
            //已经点过赞了
            operations.remove(userId);
            decrUserGetLikeCount(visitorId, userId, entityType, entityId);
        } else {
            //还没有点赞
            operations.add(userId);
            incrUserGetLikeCount(visitorId, userId, entityType, entityId);
        }
    }

    //查询点赞数量
    @Override
    public long findLikeCount(Integer entityType, Integer entityId) {
        String redisLikeKey = RedisKeyUtil.getRedisLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(redisLikeKey);
    }

    //查询某人是否给该帖子或者评论点过赞
    @Override
    public Integer isLike(Integer userId, Integer entityType, Integer entityId) {
        String redisLikeKey = RedisKeyUtil.getRedisLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(redisLikeKey, userId) ? 1 : 0;
    }

    //增加用户收到的赞
    @Override
    public void incrUserGetLikeCount(Integer visitorId, Integer userId, Integer entityType, Integer entityId) {
        String redisKey = RedisKeyUtil.getRedisUserLikeKey(visitorId);
        Map<String, Object> map = new HashMap<>();
        map.put("LikeUserId", userId);
        map.put("entityType", entityType);
        map.put("entityId", entityId);
        redisTemplate.opsForSet().add(redisKey, map);
    }

    //减少用户收到的赞，将来这个集合的value可以是map集合，可以放入点赞的内容的本体和点赞用户
    @Override
    public void decrUserGetLikeCount(Integer visitorId, Integer userId, Integer entityType, Integer entityId) {
        Map<String, Object> map = new HashMap<>();
        map.put("LikeUserId", userId);
        map.put("entityType", entityType);
        map.put("entityId", entityId);
        String redisKey = RedisKeyUtil.getRedisUserLikeKey(visitorId);
        redisTemplate.opsForSet().remove(redisKey, map);
    }

    //查询用户收到的赞的数量
    @Override
    public Long findUserGetLikeCount(Integer userId) {
        String redisKey = RedisKeyUtil.getRedisUserLikeKey(userId);
        Long count = redisTemplate.opsForSet().size(redisKey);
        return count == null ? 0 : count;
    }
}
