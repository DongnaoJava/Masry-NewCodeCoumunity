package com.bin.community;

import com.bin.bean.LoginTicket;
import com.bin.dao.TicketMapper;
import com.bin.service.TicketService;
import com.bin.util.loginUtil.VerificationCodeUtil;
import com.bin.util.mailUtil.MailSendUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;


@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    MailSendUtil mailSendUtil;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    TicketService ticketService;

    @Test
    public void TestMail() {
        mailSendUtil.Sendmail("masrybin@petalmail.com", "Test", "Welcome!");
    }

    @Test
    public void TestMail2() {
        Context context = new Context();
        context.setVariable("SendTo", "贾斌");
        String content = templateEngine.process("/mail/activation.html", context);
        mailSendUtil.Sendmail("masrybin@petalmail.com", "Test", content);
    }

    @Test
    public void TestTicket() {
        /*LoginTicket loginTicket = new LoginTicket(null,1001,"abc",0,new Date());
        ticketService.insertTicket(loginTicket);*/
        System.out.println(ticketService.selectByTicket("abc"));
        ticketService.updateStatusByTicket("abc",1);
        System.out.println(ticketService.selectByTicket("abc"));
    }
    @Test
    public void TestCode() {

    }
}
