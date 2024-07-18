package com.xiangkai.community;

import com.xiangkai.community.service.AIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class BaiduYiYanTests {

    @Autowired
    private AIService aiService;

    @Test
    void testGetToken() throws IOException {

    }

}
