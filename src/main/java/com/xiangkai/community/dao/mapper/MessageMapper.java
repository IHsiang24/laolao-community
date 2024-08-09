package com.xiangkai.community.dao.mapper;

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

    // 插入消息
    Integer insertMessage(Message message);

    // 批量更新状态
    Integer updateStatus(List<Integer> ids, Integer status);

    // 查询最新的通知
    Message selectLatestNotice(Integer userId, String topic);

    // 查询未读通知数量
    Integer   selectUnreadNoticeCount(Integer userId, String topic);

    // 查询总的通知数量
    Integer selectTotalNoticeCount(Integer userId, String topic);

    // 查询某个主题所包含的通知列表
    List<Message> selectNotices(Integer userId, String topic, Integer offset, Integer limit);
}
