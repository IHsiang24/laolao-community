package com.xiangkai.community;

import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiscussPostMapperTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void selectDiscussPosts() {
        System.out.println(discussPostMapper.selectDiscussPosts(101, 0, 100));
    }

    @Test
    void selectDiscussPostRows() {
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }
}
