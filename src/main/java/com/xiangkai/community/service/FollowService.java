package com.xiangkai.community.service;

import com.xiangkai.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void   follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String targetKey = RedisUtil.getFollowTargetKey(userId, entityType);
                String followerKey = RedisUtil.getFollowerKey(entityType, entityId);

                redisTemplate.multi();
                redisTemplate.opsForZSet().add(targetKey, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
                return redisTemplate.exec();
            }
        });
    }

    public void unFollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String targetKey = RedisUtil.getFollowTargetKey(userId, entityType);
                String followerKey = RedisUtil.getFollowerKey(entityType, entityId);

                redisTemplate.multi();
                redisTemplate.opsForZSet().remove(targetKey, entityId);
                redisTemplate.opsForZSet().remove(followerKey, userId);
                return redisTemplate.exec();
            }
        });
    }

    public long findFolloweeCount(int userId, int entityType) {
        String targetKey = RedisUtil.getFollowTargetKey(userId, entityType);
        Long size = redisTemplate.opsForZSet().size(targetKey);
        return size != null ? size : 0;
    }

    public long findFollowerCount(int entityType, int entityId) {
        String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
        Long size = redisTemplate.opsForZSet().size(followerKey);
        return size != null ? size : 0;
    }

    public boolean isFollowed(int userId, int entityType, int entityId) {
        String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
        Double score = redisTemplate.opsForZSet().score(followerKey, userId);
        return score != null;
    }
}
