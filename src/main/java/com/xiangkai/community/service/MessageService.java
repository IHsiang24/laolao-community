package com.xiangkai.community.service;

import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.mapper.MessageMapper;
import com.xiangkai.community.mapper.UserMapper;
import com.xiangkai.community.model.dto.SendLetterDTO;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.Message;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserMapper userMapper;


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

    public Result<Object> addMessage(SendLetterDTO dto) {
        Message message = new Message();
        User fromUser = hostHolder.get();
        message.setFromId(fromUser.getId());
        User toUser = userMapper.selectByUserName(dto.getToUserName());
        message.setToId(toUser.getId());
        message.setContent(HtmlUtils.htmlEscape(dto.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        message.setConversationId(generateConversationId(fromUser, toUser));
        message.setStatus(0);
        messageMapper.insertMessage(message);
        return new Result<>(ErrorCode.SUCCESS);
    }

    public Integer updateStatus(List<Integer> ids, Integer status) {
        return messageMapper.updateStatus(ids, status);
    }

    private String generateConversationId(User fromUser, User toUser) {
        Integer fromId = fromUser.getId();
        Integer toId = toUser.getId();

        int first = Math.min(fromId, toId);
        int second = Math.max(fromId, toId);

        return first + "_" + second;
    }

    public  Result<Object> addMessage(Message message) {
        messageMapper.insertMessage(message);
        return new Result<>(ErrorCode.SUCCESS);
    }
}
