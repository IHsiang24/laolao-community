package com.xiangkai.community.service;

import com.xiangkai.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void like(int entityType, int entityId, int userId, int entityUserId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
                String userKey = RedisUtil.getUserLikeKey(entityUserId);

                Boolean isMember = redisTemplate.opsForSet().isMember(entityKey, userId);
                redisTemplate.multi();
                if (isMember != null && !isMember) {
                    redisTemplate.opsForSet().add(entityKey, userId);
                    redisTemplate.opsForValue().increment(userKey);
                } else {
                    redisTemplate.opsForSet().remove(entityKey, userId);
                    redisTemplate.opsForValue().decrement(userKey);
                }
                return redisTemplate.exec();
            }
        });
    }

    public Long findEntityLikeCount(int entityType, int entityId) {
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityKey);
    }

    public Integer findEntityLikeStatus(int entityType, int entityId, int userId) {
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        Boolean member = redisTemplate.opsForSet().isMember(entityKey, userId);
        return member != null && member ? 1 : 0;
    }

    public Integer findUserLikeCount(int userId) {
        String userKey = RedisUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userKey);

        return count != null ? count : 0;
    }
}
