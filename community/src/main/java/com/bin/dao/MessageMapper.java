package com.bin.dao;

import com.bin.bean.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    //查询当前用户会话列表，每个列表只显示最新的一条消息
    List<Message> selectConversations(Integer userId, Integer offset, Integer limit);

    //嘻哈寻当前用户的会话数量
    Integer selectConversationsRows(Integer userId);

    //查询某个私信的会话列表
    List<Message> selectMessages(String conversationId, Integer offset, Integer limit);

    //查询某个私信列表的会话数量
    Integer selectMessagesRows(String conversationId);

    //查询未读私信的数量
    Integer selectMessagesUnreadRows(Integer userId,String conversationId);

    //更新一个会话中所有消息的状态（未读，已读，删除）
    Integer updateMessageStatus(String conversationId,Integer status);

    //更新一消息的状态（未读，已读，删除）
    Integer updateOneMessageStatus(Integer id,Integer status);
}
