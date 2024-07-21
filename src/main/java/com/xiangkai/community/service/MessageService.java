package com.xiangkai.community.service;

import com.xiangkai.community.mapper.MessageMapper;
import com.xiangkai.community.model.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> findConversationsByUserId(Integer userId, Integer offset, Integer limit) {
        return messageMapper.selectConversationsByUserId(userId, offset, limit);
    }

    public Integer findConversationCountByUserId(Integer userId) {
        return messageMapper.selectConversationCountByUserId(userId);
    }

    public List<Message> findLetterByConversationId(String conversationId, Integer offset, Integer limit) {
        return messageMapper.selectLetterByConversationId(conversationId, offset, limit);
    }

    public Integer findLetterCountByConversationId(String conversationId) {
        return messageMapper.selectLetterCountByConversationId(conversationId);
    }

    public Integer findUnreadLetterCountByUserId(Integer userId, String conversationId) {
        return messageMapper.selectUnreadLetterCountByUserId(userId, conversationId);
    }
}
