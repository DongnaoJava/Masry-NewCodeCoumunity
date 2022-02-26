package com.bin.bean;

public interface TicketExpiredTime {
   //默认状态账户登陆凭证记录时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    //用户勾选记住我之后的登陆凭证记录时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
