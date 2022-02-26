package com.bin.controller;

import com.bin.Annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingController {
    @LoginRequired
    @GetMapping("/setting")
    public String getSetting(){
        return "/site/setting";
    }
}
