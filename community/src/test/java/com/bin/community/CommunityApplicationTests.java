package com.bin.community;

import com.bin.util.mailUtil.MailSendUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    MailSendUtil mailSendUtil;
    @Autowired
    TemplateEngine templateEngine;

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
}
