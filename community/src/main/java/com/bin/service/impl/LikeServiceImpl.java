package com.bin.service.impl;

import com.bin.service.LikeService;
import com.bin.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //点赞
    @Override
    public void like(Integer userId, Integer entityType, Integer entityId) {
        String redisLikeKey = RedisKeyUtil.getRedisLikeKey(entityType, entityId);
        BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(redisLikeKey);
        Boolean isMember = operations.isMember(userId);
        if (isMember)
            operations.remove(userId);
        else
            operations.add(userId);
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
}
