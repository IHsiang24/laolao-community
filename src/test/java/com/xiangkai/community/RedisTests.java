package com.xiangkai.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testStrings() {
        String key = "test:count";

        redisTemplate.opsForValue().set(key, 1);
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().decrement(key));
        System.out.println(redisTemplate.opsForValue().get(key));
    }

    @Test
    void testHash() {
        String key = "test:user";
        redisTemplate.opsForHash().put(key, "id", 1);
        redisTemplate.opsForHash().put(key, "username", "zhangsan");
        System.out.println(redisTemplate.opsForHash().get(key, "id"));
        System.out.println(redisTemplate.opsForHash().get(key, "username"));
    }

    @Test
    void testList() {
        String key = "test:student";

        redisTemplate.opsForList().leftPush(key, "zhangsan");
        redisTemplate.opsForList().leftPush(key, "lisi");
        redisTemplate.opsForList().leftPush(key, "wanger");
        redisTemplate.opsForList().leftPush(key, "mazi");

        System.out.println(redisTemplate.opsForList().index(key,2));

        System.out.println(redisTemplate.opsForList().range(key, 0,3));

        System.out.println(redisTemplate.opsForList().leftPop(key));
        System.out.println(redisTemplate.opsForList().leftPop(key));
        System.out.println(redisTemplate.opsForList().leftPop(key));
    }

    @Test
    void testSet() {
        String key = "test:teacher";

        redisTemplate.opsForSet().add(key, "aaa", "bbb", "ccc", "ddd", "eee", "fff");

        System.out.println(redisTemplate.opsForSet().members(key));
        System.out.println(redisTemplate.opsForSet().pop(key));
        System.out.println(redisTemplate.opsForSet().pop(key));
        System.out.println(redisTemplate.opsForSet().pop(key));
        System.out.println(redisTemplate.opsForSet().pop(key));
    }

    @Test
    void testSortedSet() {
        String key = "test:rank";

        redisTemplate.opsForZSet().add(key, "aaa", 10);
        redisTemplate.opsForZSet().add(key, "bbb", 20);
        redisTemplate.opsForZSet().add(key, "ccc", 30);
        redisTemplate.opsForZSet().add(key, "ddd", 40);

        System.out.println(redisTemplate.opsForZSet().range(key, 0,2));
        System.out.println(redisTemplate.opsForZSet().incrementScore(key, "aaa",2));

        System.out.println(redisTemplate.opsForZSet().rank(key, "ccc"));

        redisTemplate.opsForZSet().reverseRange(key, 0, 2);
        System.out.println(redisTemplate.opsForZSet().reverseRange(key, 0,2));
    }

    @Test
    void testKey() {
        System.out.println(redisTemplate.hasKey("test:rank"));

        redisTemplate.delete("test:rank");

        System.out.println(redisTemplate.hasKey("test:rank"));
    }

    @Test
    void testBoundKey() {
        String key = "test:rank";
        BoundValueOperations<String, Object> operations = redisTemplate.boundValueOps(key);
        System.out.println(operations.increment());
        System.out.println(operations.increment());
        System.out.println(operations.increment());
        System.out.println(operations.increment());
    }

    @Test
    void testTransaction() {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                String key = "test:tx";

                operations.multi();

                redisTemplate.opsForSet().add(key, "aaa", "bbb", "ccc", "ddd", "eee", "fff");
                System.out.println(redisTemplate.opsForSet().members(key));

                operations.exec();
                return null;
            }
        });
    }

    @Test
    void testExpired() {
        redisTemplate.expire("test:tx", 10, TimeUnit.SECONDS);
    }

}
