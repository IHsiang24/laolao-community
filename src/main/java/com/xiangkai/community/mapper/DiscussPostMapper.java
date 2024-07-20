package com.xiangkai.community.mapper;

import com.xiangkai.community.model.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(Integer userId, Integer offset, Integer limit);

    Integer selectDiscussPostRows(Integer userId);

    Integer insertDiscussPost(DiscussPost post);

    DiscussPost selectById(Integer id);

    Integer updateCommentCount(Integer id, Integer commentCount);
}
