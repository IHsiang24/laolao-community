package com.xiangkai.community;

import com.xiangkai.community.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTests {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void select() {
        System.out.println(commentMapper.selectComments(1, 275, 0, 100));
    }


}
