package com.xiangkai.community;

import com.xiangkai.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveFilterTests {

    @Autowired
    private SensitiveFilter filter;

    @Test
    void testFilter() {
        String s = filter.filter("我要去和别人的人妻一边在★开★★房★，一边在看★片网开房，并且想要★★嫖★娼和赌★★博★，我完事之后，你替我去公司★★开票★★报销，听懂了吗");
        System.out.println(s);
    }
}
