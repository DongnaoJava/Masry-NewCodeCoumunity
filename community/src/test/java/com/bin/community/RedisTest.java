package com.bin.community;

import com.bin.service.impl.LikeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;

import java.util.Set;

@SpringBootTest
public class RedisTest {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    LikeServiceImpl likeService;

    @Test
    public void test01() {
        String testKey = "test:aaa";
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        stringObjectValueOperations.set(testKey, 1);
        System.out.println(stringObjectValueOperations.get(testKey));
        System.out.println(stringObjectValueOperations.increment(testKey));
        System.out.println(stringObjectValueOperations.get(testKey));
    }

    @Test
    public void test02() {
        String testKey = "test:user";
        HashOperations<String, String, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        stringObjectObjectHashOperations.put(testKey, "username", "zhangsan");
        stringObjectObjectHashOperations.put(testKey, "age", "24");
        stringObjectObjectHashOperations.put(testKey, "sex", "man");
        System.out.println(stringObjectObjectHashOperations.get(testKey, "username"));
        Set<String> keys = stringObjectObjectHashOperations.keys(testKey);
        for (String key:keys) {
            String value = (String) stringObjectObjectHashOperations.get(testKey,key);
            System.out.println(key+":"+value);
        }
    }
    @Test
    public void test03(){
       BoundValueOperations<String, Object> operations = redisTemplate.boundValueOps("test:aaa");
        System.out.println(operations.get());
        System.out.println(operations.increment());
    }
    @Test
    public void transactional(){
        Object obj = redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                BoundHashOperations boundHashOperations = operations.boundHashOps("test:user");
                boundHashOperations.put("brother","lisi");
                boundHashOperations.put("wife","wangwu");
                System.out.println(boundHashOperations.entries());
                Object obj = operations.exec();
                return obj;
            }
        });
        System.out.println(obj);
    }
    @Test
    public void test05(){
        System.out.println(likeService.findLikeCount(1,166));
    }
}
