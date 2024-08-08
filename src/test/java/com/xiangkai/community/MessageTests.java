package com.xiangkai.community;

import com.xiangkai.community.model.entity.Message;
import com.xiangkai.community.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageTests {

    @Autowired
    private MessageService messageService;

    @Test
    void findConversationsByUserId() {
        System.out.println(messageService.findConversationsByUserId(111, 0, 100));
    }

    @Test
    void findConversationCountByUserId() {
        System.out.println(messageService.findConversationCountByUserId(111));
    }

    @Test
    void findLetterByConversationId() {
        System.out.println(messageService.findLetterByConversationId("111_112", 0, 100));
    }

    @Test
    void findConversationCountByConversationId() {
        System.out.println(messageService.findLetterCountByConversationId("111_112"));
    }

    @Test
    void findUnreadLetterCountByUserId() {
        messageService.findUnreadLetterCountByUserId(111, "111_145");
    }

    @Test
    void findLatestNotice() {
        Message latestCommentMessage = messageService.findLatestNotice(112, "follow");
        System.out.println(latestCommentMessage);
    }
}
