package com.xiangkai.community;

import com.xiangkai.community.entity.User;
import com.xiangkai.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void selectById() {
        System.out.println(userMapper.selectById(101));
    }

    @Test
    void selectByName() {
        System.out.println(userMapper.selectByUserName("liubei"));
    }

    @Test
    void selectByEmail() {
        System.out.println(userMapper.selectByEmail("nowcoder101@sina.com"));
    }

    @Test
    void insertUser() {
        User user = new User()
                .setUsername("九爷")
                .setPassword("qqq111")
                .setSalt("qqq111")
                .setEmail("qqq111@sina.com")
                .setType(1)
                .setStatus(1)
                .setActivationCode("qqq111")
                .setHeaderUrl("http://static.nowcoder.com/images/head/notify.png")
                .setCreateTime(new Date())
                .build();

        System.out.println(userMapper.insertUser(user));
    }

    @Test
    void updateStatus() {
        System.out.println(userMapper.updateStatus(150, 2));
    }

    @Test
    void updateHeaderUrl() {
        System.out.println(userMapper.updateHeaderUrl(150, "http://images.nowcoder.com/head/149t.png"));
    }

    @Test
    void updatePassword() {
        System.out.println(userMapper.updatePassword(150, "111qqq"));
    }
}
