package com.xiangkai.community.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.dao.mapper.UserMapper;
import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.event.EventProducer;
import com.xiangkai.community.model.dto.DiscussPostDTO;
import com.xiangkai.community.model.dto.SetDTO;
import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.cache.HotKeyDetector;
import com.xiangkai.community.util.RedisUtil;
import com.xiangkai.community.util.SensitiveFilter;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DiscussPostService implements CommunityConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscussPostService.class);

    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private EventProducer producer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HotKeyDetector hotKeyDetector;

    @Value("${caffeine.cache.max-size}")
    private Long cacheMaxSize;

    @Value("${caffeine.cache.expired-seconds}")
    private Long cacheExpiredSeconds;

    // 缓存首页帖子概览
    private static LoadingCache<String, List<DiscussPost>> postsCache;

    // 缓存单个帖子详情
    private static LoadingCache<String, DiscussPost> hotPostsCache;

    private static LoadingCache<Integer, Integer> rowsCache;

    @PostConstruct
    private void init() {
        postsCache = Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheExpiredSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Nullable
                    @Override
                    public List<DiscussPost> load(String key) throws Exception {
                        if (StringUtils.isBlank(key)) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        String[] params = key.split(":");

                        Integer offset = Integer.valueOf(params[0]);

                        Integer limit = Integer.valueOf(params[1]);

                        LOGGER.info("load posts cache from DB!");

                        return discussPostMapper.selectDiscussPosts(0, offset, limit, 1);
                    }
                });

        rowsCache = Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheExpiredSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(Integer key) throws Exception {
                        if (key == null) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        LOGGER.info("load rows cache from DB!");
                        return discussPostMapper.selectDiscussPostRows(0);
                    }
                });

        hotPostsCache = Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheExpiredSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, DiscussPost>() {
                    @Override
                    public @Nullable DiscussPost load(@NonNull String key) throws Exception {
                        if (hotKeyDetector.isHotKey(key)) {
                            LOGGER.info("{} is hotKey! Load from caffeine",key);
                            if (redisTemplate.hasKey(key)) {
                                return (DiscussPost) redisTemplate.opsForValue().get(key);
                            }

                            String[] params = key.split(":");
                            Integer postId = Integer.valueOf(params[1]);
                            DiscussPost post = discussPostMapper.selectById(postId);
                            String hotPostKey = RedisUtil.getPostKey(postId);
                            redisTemplate.opsForValue().set(hotPostKey, post);
                            return post;
                        }
                        return null;
                    }
                });
    }

    public List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit, Integer mode) {
        if (userId == 0 && mode == 1) {
            return postsCache.get(offset + ":" + limit);
        }
        LOGGER.info("load posts from DB!");
        return discussPostMapper.selectDiscussPosts(userId, offset, limit, mode);
    }

    public Integer findDiscussPostRows(Integer userId) {
        if (userId == 0) {
            return rowsCache.get(0);
        }
        LOGGER.info("load rows from DB!");
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public Result<Object> publishDiscussPost(DiscussPostDTO dto) {

        User user = hostHolder.get();

        if (user == null) {
            return new Result<>(ErrorCode.USER_UNLOGIN);
        }

        String title = dto.getTitle();
        String content = dto.getContent();

        // 处理html标签
        String htmlEscapeTitle = HtmlUtils.htmlEscape(title);
        String htmlEscapeContent = HtmlUtils.htmlEscape(content);

        // 处理敏感词
        String titleByFilter = sensitiveFilter.filter(htmlEscapeTitle);
        String contentByFilter = sensitiveFilter.filter(htmlEscapeContent);

        DiscussPost post = new DiscussPost()
                .setUserId(user.getId())
                .setTitle(titleByFilter)
                .setContent(contentByFilter)
                .setType(0)
                .setStatus(0)
                .setCreateTime(new Date())
                .setCommentCount(0)
                .setScore(0.0)
                .build();

        Integer result = discussPostMapper.insertDiscussPost(post);

        if (result >= 0) {
            String postScoreKey = RedisUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(postScoreKey, post.getId());

            return new Result<>(ErrorCode.SUCCESS);
        }
        return new Result<>(ErrorCode.SYSTEM_INTERNAL_ERROR);
    }

    public DiscussPost findDiscussPostById(Integer id) {
        String postKey = RedisUtil.getPostKey(id);
        hotKeyDetector.recordAccess(postKey);

        DiscussPost hotPost = hotPostsCache.get(postKey);
        if (hotPost != null) {
            return hotPost;
        } else if (redisTemplate.hasKey(postKey)) {
            return (DiscussPost) redisTemplate.opsForValue().get(postKey);
        } else {
            DiscussPost discussPost = discussPostMapper.selectById(id);
            redisTemplate.opsForValue().set(postKey, discussPost);
            return discussPost;
        }
    }

    public Result<Object> top(SetDTO dto) {
        discussPostMapper.updatePostType(dto.getId(), 1);
        return new Result<>(ErrorCode.SUCCESS);
    }

    public Result<Object> wonderful(SetDTO dto) {
        discussPostMapper.updatePostStatus(dto.getId(), 1);
        return new Result<>(ErrorCode.SUCCESS);
    }
    public Result<Object> blackList(SetDTO dto) {

        discussPostMapper.updatePostStatus(dto.getId(), 2);

        User loginUser = hostHolder.get();
        DiscussPost post = discussPostMapper.selectById(dto.getId());
        User user = userMapper.selectById(post.getUserId());

        // 发布DELETE事件
        Event event = new Event.Builder()
                .eventTypeId(EVENT_TYPE_ID_DELETE)
                .topic(TOPIC_DELETE)
                .userId(loginUser.getId())
                .entityType(ENTITY_TYPE_POST)
                .entityId(dto.getId())
                .targetUserId(user.getId())
                .timestamp(System.currentTimeMillis())
                .data("discussPostId", dto.getId())
                .build();
        producer.fireEvent(event);

        return new Result<>(ErrorCode.SUCCESS);
    }

    public Integer updateScore(Integer id, Double score) {
        return discussPostMapper.updatePostScore(id, score);
    }

}
