package com.xiangkai.community.service;

import com.xiangkai.community.entity.DiscussPost;
import com.xiangkai.community.mapper.DiscussPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public Integer findDiscussPostRows(Integer userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
