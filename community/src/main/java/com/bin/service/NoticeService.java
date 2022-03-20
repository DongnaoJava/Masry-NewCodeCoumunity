package com.bin.service;

import com.bin.bean.Message;

import java.util.List;

public interface NoticeService {
    //查询当前用户收到的最新的一条某种类型的通知
    Message selectOneNewestOneTypeNotice(Integer userId,String noticeType);

    //查询当前用户收到的所有某种类型的通知
    List<Message> selectAllOneTypeNotices(Integer userId, String noticeType, Integer offset, Integer limit);

    //查询当前用户收到的所有的某种类型的通知数量
    Integer selectAllOneTypeNoticesCount(Integer userId,String noticeType);

    //查询当前用户收到的所有未读的某种类型的通知数量
    Integer selectAllUnreadOneTypeNoticesCount(Integer userId,String noticeType);

    //查寻当前用户未读通知的总数量
    Integer selectUnreadNoticesRows(Integer userId);

    //更新一条通知的状态（未读，已读，删除）
    Integer updateOneNoticeStatus(Integer id, Integer status);

    //更新一种通知的状态（未读，已读，删除）
    Integer updateOneTypeNoticeStatus(String noticeType, Integer status);
}
