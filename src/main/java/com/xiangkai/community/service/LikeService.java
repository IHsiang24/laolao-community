package com.xiangkai.community.service;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.event.EventProducer;
import com.xiangkai.community.dao.mapper.CommentMapper;
import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.Comment;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService implements CommunityConstant {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EventProducer producer;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private CommentMapper commentMapper;

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

                    /* 发送点赞类消息 */
                    Integer discussPostId = 0;

                    // 对帖子点赞
                    if (ENTITY_TYPE_POST.equals(entityType)) {
                        discussPostId = entityId;

                        String postScoreKey = RedisUtil.getPostScoreKey();
                        redisTemplate.opsForSet().add(postScoreKey, discussPostId);
                    }

                    // 对回帖点赞
                    if (ENTITY_TYPE_COMMENT.equals(entityType)) {
                        // 查找回帖
                        Comment comment = commentMapper.selectById(entityId);
                        discussPostId = comment.getEntityId();
                    }

                    // 对回复点赞
                    if (ENTITY_TYPE_REPLY.equals(entityType)) {
                        // 查找回复本身
                        Comment reply = commentMapper.selectById(entityId);

                        // 查找回复对应的回帖
                        Comment comment = commentMapper.selectById(reply.getEntityId());

                        discussPostId = comment.getEntityId();
                    }

                    Event event = new Event.Builder()
                            .eventTypeId(EVENT_TYPE_ID_LIKE)
                            .topic(TOPIC_LIKE)
                            .userId(userId)
                            .entityType(entityType)
                            .entityId(entityId)
                            .targetUserId(entityUserId)
                            .timestamp(System.currentTimeMillis())
                            .data("discussPostId", discussPostId)
                            .build();

                    producer.fireEvent(event);
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
