package com.bin.service.impl;

import com.bin.service.FollowService;
import com.bin.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserService userService;

    //关注
    @Override
    public void follow(Integer userId, Integer entityType, Integer entityId) {
        String redisFolloweeKey = RedisKeyUtil.getRedisFolloweeKey(userId, entityType);
        String redisFollowerKey = RedisKeyUtil.getRedisFollowerKey(entityType, entityId);

        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                long time = new Date().getTime();
                operations.opsForZSet().add(redisFolloweeKey, entityId, time);
                operations.opsForZSet().add(redisFollowerKey, userId, time);
                return operations.exec();
            }
        });
    }

    //取消关注
    @Override
    public void cancelFollow(Integer userId, Integer entityType, Integer entityId) {
        String redisFolloweeKey = RedisKeyUtil.getRedisFolloweeKey(userId, entityType);
        String redisFollowerKey = RedisKeyUtil.getRedisFollowerKey(entityType, entityId);

        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForZSet().remove(redisFolloweeKey, entityId);
                operations.opsForZSet().remove(redisFollowerKey, userId);
                return operations.exec();
            }
        });
    }

    //获取某个实体的关注者数量
    @Override
    public Long getFollowerCount(Integer entityType, Integer entityId) {
        String redisFollowerKey = RedisKeyUtil.getRedisFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(redisFollowerKey);
    }

    //获取某个用户关注的实体的个数，要分类的，每个类都有自己的数量
    @Override
    public Long getFolloweeCount(Integer userId, Integer entityType) {
        String redisFolloweeKey = RedisKeyUtil.getRedisFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(redisFolloweeKey);
    }

    //获取是否关注某个实体
    @Override
    public Integer getStatusOfFollow(Integer userId, Integer entityType, Integer entityId) {
        String redisFollowerKey = RedisKeyUtil.getRedisFollowerKey(entityType, entityId);
        String redisFolloweeKey = RedisKeyUtil.getRedisFolloweeKey(userId, entityType);

        //判断实体的粉丝里有没有该用户
        boolean followeeIsHave = false;
        Long rank = redisTemplate.opsForZSet().rank(redisFollowerKey, userId);
        if (rank != null) {
            Set<Object> userIds = redisTemplate.opsForZSet().range(redisFollowerKey, rank, rank);
            if (userIds != null)
                for (Object id : userIds) {
                    if (id.equals(userId)) {
                        followeeIsHave = true;
                        break;
                    }
                }
        }

        //判断用户的关注对象里有没有该实体
        boolean followerIsHave = false;
        Long rank1 = redisTemplate.opsForZSet().rank(redisFolloweeKey, entityId);
        if (rank1 != null) {
            Set<Object> entityIds = redisTemplate.opsForZSet().range(redisFolloweeKey, rank1, rank1);
            if (entityIds != null)
                for (Object id : entityIds) {
                    if (id.equals(entityId)) {
                        followerIsHave = true;
                        break;
                    }
                }
        }
        if (followeeIsHave && followerIsHave)
            //已关注
            return 0;
        //未关注
        return 1;
    }

    //获取用户关注的所有对象
    @Override
    public List<Map<String, Object>> getFollowee(Integer userId, Integer entityType,Integer offset,Integer limit) {
        String redisFolloweeKey = RedisKeyUtil.getRedisFolloweeKey(userId, entityType);
        Set<Object> entityIds = redisTemplate.opsForZSet().reverseRange(redisFolloweeKey, offset, offset+limit-1);
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (entityIds != null)
            for (Object entityId : entityIds) {
                Map<String, Object> map = new HashMap<>();
                //目前只考虑了人
                map.put("user", userService.selectUserById((Integer) entityId));
                long score = Objects.requireNonNull(redisTemplate.opsForZSet().score(redisFolloweeKey, entityId)).longValue();
                map.put("createTime", new Date(score));
                mapList.add(map);
            }
        return mapList;
    }
}
