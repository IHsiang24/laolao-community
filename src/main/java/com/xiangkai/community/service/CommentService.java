package com.xiangkai.community.service;

import com.xiangkai.community.annotation.LoginRequired;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.mapper.CommentMapper;
import com.xiangkai.community.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.Comment;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class CommentService implements CommunityConstant {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Comment> findComments(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        return commentMapper.selectComments(entityType, entityId, offset, limit);
    }

    public Integer findCommentRows(Integer entityType, Integer entityId) {
        return commentMapper.selectCommentRows(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addComment(Integer discussPostId, Comment comment) {

        User user = hostHolder.get();
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        comment.setUserId(user.getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);

        if (ENTITY_TYPE_COMMENT.equals(comment.getEntityType())) {
            Integer commentCount = commentMapper.selectCommentRows(ENTITY_TYPE_COMMENT,
                    comment.getEntityId());

            discussPostMapper.updateCommentCount(comment.getEntityId(), commentCount);
        }

    }

}
