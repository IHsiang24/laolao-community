package com.xiangkai.community.service;

import com.xiangkai.community.mapper.CommentMapper;
import com.xiangkai.community.model.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    public List<Comment> findComments(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        return commentMapper.selectComments(entityType, entityId, offset, limit);
    }

    public Integer findCommentRows(Integer entityType, Integer entityId) {
        return commentMapper.selectCommentRows(entityType, entityId);
    }

}
