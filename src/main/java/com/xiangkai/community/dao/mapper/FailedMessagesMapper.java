package com.xiangkai.community.dao.mapper;

import com.xiangkai.community.model.entity.FailedMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FailedMessagesMapper {

    FailedMessage selectFailedMessageById(Integer id);

    Integer insertFailedMessage(FailedMessage failedMessage);
}
