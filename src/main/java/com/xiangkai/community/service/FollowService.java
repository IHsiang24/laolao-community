package com.xiangkai.community.service;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.event.EventProducer;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer producer;

    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String targetKey = RedisUtil.getFollowTargetKey(userId, entityType);
                String followerKey = RedisUtil.getFollowerKey(entityType, entityId);

                redisTemplate.multi();
                redisTemplate.opsForZSet().add(targetKey, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                Event event = new Event.Builder()
                        .eventTypeId(EVENT_TYPE_ID_FOLLOW)
                        .topic(TOPIC_FOLLOW)
                        .userId(userId)
                        .entityType(entityType)
                        .entityId(entityId)
                        .targetUserId(entityId)
                        .timestamp(System.currentTimeMillis())
                        .build();

                producer.fireEvent(event);
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

    public List<Map<String, Object>> findFolloweeList(int userId, int entityType, int offset, int limit) {
        String targetKey = RedisUtil.getFollowTargetKey(userId, entityType);
        Set<Object> followeeIds = redisTemplate.opsForZSet().range(targetKey, offset, offset + limit - 1);
        if (followeeIds == null) {
            return null;
        }
        List<Map<String, Object>> followeeList = new ArrayList<>();
        for (Object o : followeeIds) {
            Map<String, Object> map = new HashMap<>();
            Integer id = (Integer) o;
            User user = userService.findUserById(id);
            map.put("user", user);

            Double score = redisTemplate.opsForZSet().score(targetKey, o);
            Date followTime = new Date(score.longValue());
            map.put("followTime",followTime);

            // 指的是登录用户是否关注该用户（查看别人的主页时）
            if (hostHolder.get() != null) {
                boolean followed = isFollowed(hostHolder.get().getId(), ENTITY_TYPE_USER, id);
                map.put("followed", followed);
            }
            followeeList.add(map);
        }
        return followeeList;
    }

    public List<Map<String, Object>> findFollowerList(int userId, int entityType, int offset, int limit) {
        String followerKey = RedisUtil.getFollowerKey(entityType, userId);
        Set<Object> followerIds = redisTemplate.opsForZSet().range(followerKey, offset, offset + limit - 1);
        if (followerIds == null) {
            return null;
        }
        List<Map<String, Object>> followerList = new ArrayList<>();
        for (Object o : followerIds) {
            Map<String, Object> map = new HashMap<>();
            Integer id = (Integer) o;
            User user = userService.findUserById(id);
            map.put("user", user);

            Double score = redisTemplate.opsForZSet().score(followerKey, o);
            Date followTime = new Date(score.longValue());
            map.put("followTime",followTime);

            // 指的是登录用户是否关注该用户（查看别人的主页时）
            if (hostHolder.get() != null) {
                boolean followed = isFollowed(hostHolder.get().getId(), ENTITY_TYPE_USER, id);
                map.put("followed", followed);
            }
            followerList.add(map);
        }
        return followerList;
    }
}
