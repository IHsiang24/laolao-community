package com.xiangkai.community.dao.mapper;

import com.xiangkai.community.model.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Deprecated
public interface LoginTicketMapper {

    LoginTicket selectByTicket(String ticket);

    Integer updateStatus(Integer userId, Integer status);

    Integer updateStatusByTicket(String ticket, Integer status);

    Integer insertTicket(LoginTicket ticket);

}
