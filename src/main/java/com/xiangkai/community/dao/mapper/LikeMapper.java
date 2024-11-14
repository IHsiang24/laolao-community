package com.xiangkai.community.dao.mapper;

import com.xiangkai.community.model.entity.Like;
import org.apache.ibatis.annotations.Mapper;

public interface LikeMapper {

    Integer insert(Like like);

    Integer deleteById(Integer id);

    Integer updateById(Integer id);

    Like selectById(Integer id);
}
