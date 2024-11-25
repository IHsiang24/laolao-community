package com.xiangkai.community.dao.mapper;

import com.xiangkai.community.model.entity.EsSyncSucceedDiscussPost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EsSyncSucceedDiscussPostMapper {

    // todo 实现发帖插入帖子表时，插入一条status为0的行，同步至es时status更改为1
    Integer insert(EsSyncSucceedDiscussPost esSyncSucceedDiscussPost);

    EsSyncSucceedDiscussPost selectById(Integer id);
}
