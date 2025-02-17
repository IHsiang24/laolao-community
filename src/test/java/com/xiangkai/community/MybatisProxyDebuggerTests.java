package com.xiangkai.community;

import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.DiscussPost;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MybatisProxyDebuggerTests {

    private static final Logger logger = LoggerFactory.getLogger(MybatisProxyDebuggerTests.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void testMybatisProxyDebugger() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DiscussPostMapper discussPostMapper = sqlSession.getMapper(DiscussPostMapper.class);
            List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10, 0);
            for (DiscussPost discussPost : discussPosts) {
                logger.info(discussPost.toString());
            }
        }
    }
}
