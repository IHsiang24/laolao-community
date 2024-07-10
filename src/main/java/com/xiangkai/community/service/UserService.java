package com.xiangkai.community.service;

import com.xiangkai.community.entity.User;
import com.xiangkai.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

}
