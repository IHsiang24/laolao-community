package com.xiangkai.community.mapper;

import com.xiangkai.community.model.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {

    LoginTicket selectByTicket(String ticket);

    Integer updateStatus(Integer userId, Integer status);

    Integer insertTicket(LoginTicket ticket);

}
