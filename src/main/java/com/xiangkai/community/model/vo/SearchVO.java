package com.xiangkai.community.model.vo;

import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.model.entity.User;
import lombok.Data;

@Data
public class SearchVO {

    private DiscussPost post;

    private User user;

    private Long likeCount;

    private Integer commentCount;
}
