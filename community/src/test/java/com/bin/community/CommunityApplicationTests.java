package com.bin.community;

import com.bin.service.TicketService;
import com.bin.util.MailSendUtil;
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
        try {
           Annotation A = Class.forName("com.bin.controller.HomeController").getAnnotation(Component.class);
            System.out.println(A==null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
