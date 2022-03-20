package com.bin.service.impl;

import com.bin.bean.Message;
import com.bin.dao.NoticeMapper;
import com.bin.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public Message selectOneNewestOneTypeNotice(Integer userId, String noticeType) {
        return noticeMapper.selectOneNewestOneTypeNotice(userId,noticeType);
    }

    @Override
    public List<Message> selectAllOneTypeNotices(Integer userId, String noticeType, Integer offset, Integer limit) {
        return noticeMapper.selectAllOneTypeNotices(userId, noticeType, offset, limit);
    }

    @Override
    public Integer selectAllOneTypeNoticesCount(Integer userId, String noticeType) {
        return noticeMapper.selectAllOneTypeNoticesCount(userId, noticeType);
    }

    @Override
    public Integer selectAllUnreadOneTypeNoticesCount(Integer userId, String noticeType) {
        return noticeMapper.selectAllUnreadOneTypeNoticesCount(userId, noticeType);
    }

    @Override
    public Integer selectUnreadNoticesRows(Integer userId) {
        return noticeMapper.selectUnreadNoticesRows(userId);
    }

    @Override
    public Integer updateOneNoticeStatus(Integer id, Integer status) {
        return noticeMapper.updateOneNoticeStatus(id, status);
    }

    @Override
    public Integer updateOneTypeNoticeStatus(String noticeType, Integer status) {
        return noticeMapper.updateOneTypeNoticeStatus(noticeType,status);
    }
}
