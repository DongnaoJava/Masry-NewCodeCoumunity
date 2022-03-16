package com.bin.bean;

public interface CommunityConstant {
    //默认状态账户登陆凭证记录时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    //用户勾选记住我之后的登陆凭证记录时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 类型：用户
     */
    int ENTITY_TYPE_USER = 0;

    /**
     * 实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型：评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 私信消息状态：未读
     */
    int MESSAGE_STATE_UNREAD = 0;
    /**
     * 私信消息状态：已读
     */
    int MESSAGE_STATE_READ = 1;
    /**
     * 私信消息状态：删除
     */
    int MESSAGE_STATE_DELETED = 2;
}
