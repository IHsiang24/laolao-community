package com.xiangkai.community.mapper;

import com.xiangkai.community.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectComments(Integer entityType, Integer entityId, Integer offset, Integer limit);

    Integer selectCommentRows(Integer entityType, Integer entityId);
}
