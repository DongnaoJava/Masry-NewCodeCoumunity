package com.bin.service.impl;

import com.bin.bean.LoginTicket;
import com.bin.dao.TicketMapper;
import com.bin.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper ticketMapper;

    //插入一条登录数据
    @Override
    public int insertTicket(LoginTicket loginTicket) {
        if (loginTicket != null)
            return ticketMapper.insertTicket(loginTicket);
        return 0;
    }

    //根据ticket查询一条登陆数据
    @Override
    public LoginTicket selectByTicket(String ticket) {
        if (ticket != null)
            return ticketMapper.selectByTicket(ticket);
        return null;
    }

    //根据ticket更新用户状态，其中1为退出登录，0为在线
    @Override
    public int updateStatusByTicket(String ticket, Integer status) {
        return ticketMapper.updateStatusByTicket(ticket, status);
    }

}
