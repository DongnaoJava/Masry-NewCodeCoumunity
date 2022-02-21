package com.bin.controller;

import com.bin.bean.User;
import com.bin.service.impl.UserService;
import com.bin.util.loginUtil.VerificationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String getRegisterHtml() {
        return "site/register";
    }

    @GetMapping("/login")
    public String getLogin() {
        VerificationCodeUtil.saveVerificationCodeImg();
        return "site/login";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        Map<String, String> mapInfo = userService.judgeUserRegisterInfo(user);
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
}
