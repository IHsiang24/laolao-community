package com.xiangkai.community.service;

import com.xiangkai.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void like(int entityType, int entityId, int userId) {
        String entityKey = RedisUtil.getEntityKey(entityType, entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(entityKey, userId);
        if (isMember != null && !isMember) {
            redisTemplate.opsForSet().add(entityKey, userId);
        } else {
            redisTemplate.opsForSet().remove(entityKey, userId);
        }
    }

    public Long findEntityLikeCount(int entityType, int entityId) {
        String entityKey = RedisUtil.getEntityKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityKey);
    }

    public Integer findEntityLikeStatus(int entityType, int entityId, int userId) {
        String entityKey = RedisUtil.getEntityKey(entityType, entityId);
        Boolean member = redisTemplate.opsForSet().isMember(entityKey, userId);
        return  member != null && member ? 1 : 0;
    }
}
