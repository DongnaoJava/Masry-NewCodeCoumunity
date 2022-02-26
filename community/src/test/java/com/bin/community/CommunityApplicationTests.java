package com.bin.community;

import com.bin.service.TicketService;
import com.bin.util.MailSendUtil;
import com.bin.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.lang.annotation.Annotation;
import java.util.*;


@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    MailSendUtil mailSendUtil;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    TicketService ticketService;
    @Autowired
    SensitiveFilter sensitiveFilter;

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
        ticketService.updateStatusByTicket("abc", 1);
        System.out.println(ticketService.selectByTicket("abc"));
    }

    @Test
    public void TestCode() throws InterruptedException {
        System.out.println(new Date().getTime());
        Thread.sleep(3000);
        System.out.println(new Date().getTime());
    }

    @Test
    public void Test() {
        System.out.println(sensitiveFilter.filter("你是煞笔，傻逼，傻吊，二比，二逼，操你妈，操他妈，草泥马"));
    }

}
