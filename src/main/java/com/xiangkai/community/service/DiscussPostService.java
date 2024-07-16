package com.xiangkai.community.service;

import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.model.dto.DiscussPostDTO;
import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class DiscussPostService {

    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private HostHolder hostHolder;

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

        DiscussPost post = new DiscussPost()
                .setUserId(user.getId())
                .setTitle(htmlEscapeTitle)
                .setContent(htmlEscapeContent)
                .setType(0)
                .setStatus(0)
                .setCreateTime(new Date())
                .setCommentCount(0)
                .setScore(0.0)
                .build();

        discussPostMapper.insertDiscussPost(post);

        return new Result<>(ErrorCode.SUCCESS);
    }

    public DiscussPost findDiscussPostById(Integer id) {
        return discussPostMapper.selectById(id);
    }
}
