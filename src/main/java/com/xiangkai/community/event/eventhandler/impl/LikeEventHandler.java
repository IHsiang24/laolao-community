package com.xiangkai.community.event.eventhandler.impl;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.event.eventhandler.AbstractEventHandler;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.model.entity.Message;
import com.xiangkai.community.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LikeEventHandler extends AbstractEventHandler implements CommunityConstant {

    @Autowired
    private MessageService messageService;

    @Override
    public void handle(Event event) {
        Message message = new Message.Builder()
                .fromId(SYSTEM_USER_ID)
                .toId(event.getTargetUserId())
                .conversationId(CONVERSATION_ID_LIKE)
                .content(event.toJson())
                .status(0)
                .createTime(new Date(event.getTimestamp()))
                .build();

        messageService.addMessage(message);
    }

    @Override
    public String getType() {
        return TOPIC_LIKE;
    }
}
