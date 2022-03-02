package com.bin.bean;

public interface CommunityConstant {
    //默认状态账户登陆凭证记录时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    //用户勾选记住我之后的登陆凭证记录时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;


    /**
     * 实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型：评论
     */
    int ENTITY_TYPE_COMMENT = 2;
}
