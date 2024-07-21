package com.xiangkai.community.mapper;

import com.xiangkai.community.model.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    // 查询当前用户的会话列表，每个会话只返回最新的消息
    List<Message> selectConversationsByUserId(Integer userId, Integer offset, Integer limit);

    // 查询会话数量
    Integer selectConversationCountByUserId(Integer userId);

    // 查询会话对应的私信列表
    List<Message> selectLetterByConversationId(String conversationId, Integer offset, Integer limit);

    // 查询会话对应的私信数量
    Integer selectLetterCountByConversationId(String conversationId);

    // 查询未读消息数量
    Integer selectUnreadLetterCountByUserId(Integer userId, String conversationId);
}
