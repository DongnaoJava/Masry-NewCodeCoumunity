package com.bin.service.impl;

import com.bin.bean.Message;
import com.bin.dao.MessageMapper;
import com.bin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> selectConversations(Integer userId, Integer offset, Integer limit) {
        if (userId == null)
            throw new IllegalArgumentException("参数异常！");
        return messageMapper.selectConversations(userId, offset, limit);
    }

    @Override
    public Integer selectConversationsRows(Integer userId) {
        if (userId == null)
            throw new IllegalArgumentException("参数异常！");
        return messageMapper.selectConversationsRows(userId);
    }

    @Override
    public List<Message> selectMessages(String conversationId, Integer offset, Integer limit) {
        if (conversationId == null)
            throw new IllegalArgumentException("参数异常！");
        return messageMapper.selectMessages(conversationId, offset, limit);
    }

    @Override
    public Integer selectMessagesRows(String conversationId) {
        if (conversationId == null)
            throw new IllegalArgumentException("参数异常！");
        return messageMapper.selectMessagesRows(conversationId);
    }

    @Override
    public Integer selectAllMessagesUnreadRows(Integer userId) {
        if (userId == null)
            throw new IllegalArgumentException("参数异常！");
        return messageMapper.selectMessagesUnreadRows(userId, null);
    }

    @Override
    public Integer selectMessagesUnreadRows(Integer userId, String conversationId) {
        if (userId == null)
            throw new IllegalArgumentException("参数异常！");
        return messageMapper.selectMessagesUnreadRows(userId, conversationId);
    }

    @Override
    public Integer updateMessageStatus(String conversationId, Integer status) {
        return messageMapper.updateMessageStatus(conversationId,status);
    }

    @Override
    public Integer updateOneMessageStatus(Integer id, Integer status) {
        return messageMapper.updateOneMessageStatus(id,status);
    }

    @Override
    public Integer insertNewMessage(Message message) {
        if (message==null)
            throw new IllegalArgumentException("参数非法！");
        return messageMapper.insertNewMessage(message);
    }
}
