package com.xiangkai.community.dao.mapper;

import com.xiangkai.community.model.entity.Follow;
import org.apache.ibatis.annotations.Mapper;

public interface FollowMapper {

    Integer insert(Follow follow);

    Integer deleteById(Integer id);

    Integer updateById(Integer id);

    Follow selectById(Integer id);
    
}
