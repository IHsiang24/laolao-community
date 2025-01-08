package com.xiangkai.community;

import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.DiscussPost;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiscussPostMapperTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void selectDiscussPosts() {
        SqlSession sqlSession = null;
        long start1 = System.currentTimeMillis();
        System.out.println(discussPostMapper.selectDiscussPosts(101, 0, 100, 0));
        System.out.println("首次查询耗时：" + (System.currentTimeMillis() - start1)/1000 + "秒");

        long start2 = System.currentTimeMillis();
        System.out.println(discussPostMapper.selectDiscussPosts(101, 0, 100, 0));
        System.out.println("第二次查询耗时：" + (System.currentTimeMillis() - start2)/1000 + "秒");

    }

    @Test
    void selectDiscussPostRows() {
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }

    @Test
    void setStatus() {
        discussPostMapper.updatePostStatus(289, 1);
    }

    @Test
    void setType() {
        discussPostMapper.updatePostType(289, 1);
    }
}
