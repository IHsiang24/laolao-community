package com.xiangkai.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
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

    // 统计20万个重复数据的独立总数.
    @Test
    public void testHyperLogLog() {
        String redisKey = "test:hll:01";

        for (int i = 1; i <= 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }

        for (int i = 1; i <= 100000; i++) {
            int r = (int) (Math.random() * 100000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey, r);
        }

        long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size);
    }

    // 将3组数据合并, 再统计合并后的重复数据的独立总数.
    @Test
    public void testHyperLogLogUnion() {
        String redisKey2 = "test:hll:02";
        for (int i = 1; i <= 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2, i);
        }

        String redisKey3 = "test:hll:03";
        for (int i = 5001; i <= 15000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3, i);
        }

        String redisKey4 = "test:hll:04";
        for (int i = 10001; i <= 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4, i);
        }

        String unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey, redisKey2, redisKey3, redisKey4);

        long size = redisTemplate.opsForHyperLogLog().size(unionKey);
        System.out.println(size);
    }

    // 统计一组数据的布尔值
    @Test
    public void testBitMap() {
        String redisKey = "test:bm:01";

        // 记录
        redisTemplate.opsForValue().setBit(redisKey, 1, true);
        redisTemplate.opsForValue().setBit(redisKey, 4, true);
        redisTemplate.opsForValue().setBit(redisKey, 7, true);

        // 查询
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 2));

        // 统计
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);
    }

    // 统计3组数据的布尔值, 并对这3组数据做OR运算.
    @Test
    public void testBitMapOperation() {
        String redisKey2 = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey2, 0, true);
        redisTemplate.opsForValue().setBit(redisKey2, 1, true);
        redisTemplate.opsForValue().setBit(redisKey2, 2, true);

        String redisKey3 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey3, 2, true);
        redisTemplate.opsForValue().setBit(redisKey3, 3, true);
        redisTemplate.opsForValue().setBit(redisKey3, 4, true);

        String redisKey4 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey4, 4, true);
        redisTemplate.opsForValue().setBit(redisKey4, 5, true);
        redisTemplate.opsForValue().setBit(redisKey4, 6, true);

        String redisKey = "test:bm:or";
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(), redisKey2.getBytes(), redisKey3.getBytes(), redisKey4.getBytes());
                return connection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);

        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 3));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 4));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 5));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 6));
    }

}
