package com.bin.dao;

import com.bin.bean.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TicketMapper {
    int insertTicket(LoginTicket loginTicket);

    LoginTicket selectByTicket(String ticket);

    @Update("update login_ticket set status = #{status} where ticket = #{ticket}")
    int updateStatusByTicket(String ticket, Integer status);
}
