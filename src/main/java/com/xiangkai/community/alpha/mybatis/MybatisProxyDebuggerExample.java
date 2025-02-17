package com.xiangkai.community.alpha.mybatis;

import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.DiscussPost;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisProxyDebuggerExample {

    private static final Logger logger = LoggerFactory.getLogger(MybatisProxyDebuggerExample.class);

    public static void main(String[] args) {
        String xmlResource = "D:\\Data\\OpenResourceProjects\\nowcoder-community\\src\\main\\java\\com\\xiangkai\\community\\mybatis-config.xml";

        try (InputStream resource = Resources.getResourceAsStream(xmlResource)) {

            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource);
            try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
                DiscussPostMapper mapper = sqlSession.getMapper(DiscussPostMapper.class);
                List<DiscussPost> discussPosts = mapper.selectDiscussPosts(0, 0, 10, 0);
                for (DiscussPost post : discussPosts) {
                    logger.info(post.toString());
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
