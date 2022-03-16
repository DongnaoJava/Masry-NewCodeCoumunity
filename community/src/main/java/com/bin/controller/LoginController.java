package com.bin.controller;

import com.bin.bean.ActivationConsequence;
import com.bin.bean.LoginTicket;
import com.bin.bean.CommunityConstant;
import com.bin.bean.User;
import com.bin.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserServiceImpl userServiceImpl;
   /* @Autowired
    private TicketServiceImpl ticketService;*/

    @GetMapping("/register")
    public String getRegisterHtml() {
        return "site/register";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "site/login";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        Map<String, String> mapInfo = userServiceImpl.judgeUserRegisterInfo(user);
        if (mapInfo == null | mapInfo.isEmpty()) {
            model.addAttribute("successInfo", "已经向您邮箱发送了一封邮件，请您确认注册账号信息！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("accountInfo", mapInfo.get("accountInfo"));
            model.addAttribute("passwordInfo", mapInfo.get("passwordInfo"));
            model.addAttribute("emailInfo", mapInfo.get("emailInfo"));
            model.addAttribute("user", user);
            return "/site/register";
        }
    }

    @GetMapping("/activation/{id}/{activationCode}")
    public String activateAccount(@PathVariable("id") Integer id, @PathVariable("activationCode") String activationCode, Model model) {
        Enum<ActivationConsequence> activationConsequence = userServiceImpl.activation(id, activationCode);
        if (ActivationConsequence.ACTIVATION_SUCCESS.equals(activationConsequence)) {
            model.addAttribute("successInfo", "您的账号已经激活成功,可以正常使用了！");
            model.addAttribute("target", "/login");
        } else if (ActivationConsequence.ACTIVATION_REPEAT.equals(activationConsequence)) {
            model.addAttribute("successInfo", "您的账号已经激活过了,无需重复激活！");
            model.addAttribute("target", "/login");
        } else {
            model.addAttribute("successInfo", "您的账号信息未激活成功，请您查询是否注册成功！");
            model.addAttribute("target", "/register");
        }
        System.out.println(userServiceImpl.selectUserById(id));
        return "/site/operate-result";
    }

    @PostMapping("/login")
    public String login(@CookieValue(value = "ticket", required = false) String ticket,
                        @CookieValue(value = "verificationCodeOwner",required = false) String verificationCodeOwner,
                        HttpServletResponse response, String username, String password,
                        String verificationCode, Model model,  boolean remember) {
        String target = null;
        if (StringUtils.isBlank(ticket)) {
            //说明第一次登陆
            int expiredTime = remember ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
            Map<String, String> mapInfo = userServiceImpl.judgeUserLoginInfo(username, password, expiredTime);
            if (mapInfo.get("ticket") != null)
                target = userServiceImpl.firstLogin(username, password, model,verificationCodeOwner, verificationCode, response, remember);
        } else {
            //说明第二次登陆
            LoginTicket loginTicket = userServiceImpl.selectByTicket(ticket);
            if (loginTicket != null) {
                if (loginTicket.getExpired().after(new Date())&&loginTicket.getStatus()==0) {
                    target = "index";
                } else
                    target = userServiceImpl.firstLogin(username, password, model,verificationCodeOwner, verificationCode, response, remember);
            } else
                target = userServiceImpl.firstLogin(username, password, model, verificationCodeOwner, verificationCode, response, remember);

        }
        //到login（携带参数）get方式 return
        if ("login".equals(target)) {
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            return "site/login";
        }
        //重定向到index get方式
        else
            return "redirect:index";
    }
//退出登录!
    @GetMapping("/logout")
    public String logout(@CookieValue(value = "ticket", required = false) String ticket, Model model) {
        if (ticket == null) {
            model.addAttribute("logInfo", "您还没有登录！");
        } else {
            LoginTicket l = userServiceImpl.selectByTicket(ticket);
            if (l == null)
                model.addAttribute("logInfo", "您还没有登录！");
            else {
                if (l.getStatus() == 1)
                    model.addAttribute("logInfo", "您还没有登录！");
                else {
                    userServiceImpl.logout(ticket);
                    model.addAttribute("logInfo", "您已退出登录！");
                }
            }
        }
        return "redirect:index";
    }
}
