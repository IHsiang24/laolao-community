package com.xiangkai.community;

import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import com.xiangkai.community.model.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@SpringBootTest
public class TransactionalManagerTests {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalManagerTests.class);

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    @Transactional
    public void testTransactionalAnnotation() {
        DiscussPost post = new DiscussPost()
                .setUserId(111)
                .setTitle("测试Transactional注解-标题")
                .setContent("测试Transactional注解-内容")
                .setType(0)
                .setStatus(0)
                .setCreateTime(new Date())
                .setCommentCount(0)
                .setScore(0.0)
                .build();
        discussPostMapper.insertDiscussPost(post);
        logger.info("执行完毕！");
    }
}
