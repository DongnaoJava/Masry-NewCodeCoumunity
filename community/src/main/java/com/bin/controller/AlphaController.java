package com.bin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AlphaController {
    @ResponseBody
    @GetMapping("/cookie")
    public String getCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + ":" + cookie.getValue());
            }
        }
        Cookie cookie1 = new Cookie("name", "贾斌");
        cookie1.setPath("/cookie");
        cookie1.setMaxAge(60);
        Cookie cookie2 = new Cookie("age", "23");
        Cookie cookie3 = new Cookie("sex", "男");
        response.addCookie(cookie1);
        response.addCookie(cookie2);
        response.addCookie(cookie3);
        return "ok";
    }
}
