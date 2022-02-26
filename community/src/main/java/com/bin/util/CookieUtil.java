package com.bin.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;

@Slf4j
public class CookieUtil {
    public static String getValueByKeyFromCookie(String key, Cookie[] cookies) {
        String value = null;
        if (cookies == null || key == null)
            log.error("cookies或者key为null!");
        else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }
}
