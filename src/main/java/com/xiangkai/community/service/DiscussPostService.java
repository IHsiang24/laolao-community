package com.xiangkai.community.service;

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
import com.xiangkai.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class DiscussPostService implements CommunityConstant {

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

    public List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public Integer findDiscussPostRows(Integer userId) {
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

        discussPostMapper.insertDiscussPost(post);

        // 发布PUBLISH事件
        Event event = new Event.Builder()
                .eventTypeId(EVENT_TYPE_ID_PUBLISH)
                .topic(TOPIC_PUBLISH)
                .userId(user.getId())
                .entityType(ENTITY_TYPE_POST)
                .entityId(post.getId())
                .targetUserId(user.getId())
                .timestamp(System.currentTimeMillis())
                .data("discussPostId", post.getId())
                .build();

        producer.fireEvent(event);

        return new Result<>(ErrorCode.SUCCESS);
    }

    public DiscussPost findDiscussPostById(Integer id) {
        return discussPostMapper.selectById(id);
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

}
