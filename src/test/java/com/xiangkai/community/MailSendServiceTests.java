package com.xiangkai.community;

import com.xiangkai.community.alpha.service.MailSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailSendServiceTests {
    @Autowired
    private MailSendService mailSendService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void sendTextTest() {
        mailSendService.send("2939059539@qq.com", "测试邮件", "这是一封测试邮件！");
    }

    @Test
    void sendHtmlTest() {
        Context context = new Context();
        context.setVariable("username", "John");
        String html = templateEngine.process("/mail/demo", context);
        mailSendService.send("ihsiang168@163.com", "测试邮件", html);
    }
}
