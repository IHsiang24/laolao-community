package com.xiangkai.community.dao.mapper;

import com.xiangkai.community.model.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(Integer userId, Integer offset, Integer limit, Integer mode);

    Integer selectDiscussPostRows(Integer userId);

    Integer insertDiscussPost(DiscussPost post);

    DiscussPost selectById(Integer id);

    Integer updateCommentCount(Integer id, Integer commentCount);

    Integer updatePostStatus(Integer id, Integer status);

    Integer updatePostType(Integer id, Integer type);

    Integer updatePostScore(Integer id, Double score);
}
