package com.bin.service;

import com.bin.bean.LoginTicket;

@Deprecated
public interface TicketService {
    int insertTicket(LoginTicket loginTicket);
    LoginTicket selectByTicket(String ticket);
    int updateStatusByTicket(String ticket,Integer status);

}
