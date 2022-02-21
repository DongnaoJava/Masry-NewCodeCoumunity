package com.bin.service.impl;

import com.bin.bean.LoginTicket;
import com.bin.dao.TicketMapper;
import com.bin.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    TicketMapper ticketMapper;

    @Override
    public int insertTicket(LoginTicket loginTicket) {
        if (loginTicket != null)
            return ticketMapper.insertTicket(loginTicket);
        return 0;
    }

    @Override
    public LoginTicket selectByTicket(String ticket) {
        if (ticket != null)
            return ticketMapper.selectByTicket(ticket);
        return null;
    }

    @Override
    public int updateStatusByTicket(String ticket, Integer status) {
        return ticketMapper.updateStatusByTicket(ticket, status);
    }

}
