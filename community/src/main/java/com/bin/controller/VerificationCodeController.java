package com.bin.controller;

import com.bin.util.CommunityUtil;
import com.bin.util.RedisKeyUtil;
import com.bin.util.VerificationCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Controller
public class VerificationCodeController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ResponseBody
    @GetMapping("/code")
    public void getCode(HttpServletResponse response, @CookieValue(value = "verificationCodeOwner", required = false) String verificationCodeOwner) {
        String randomCode = VerificationCodeUtil.getRandomCode(4);

        if (StringUtils.isBlank(verificationCodeOwner)) {
            //将验证码的owner放入cookie
            verificationCodeOwner = CommunityUtil.generateUUID();
            Cookie verificationCodeOwnerCookie = new Cookie("verificationCodeOwner", verificationCodeOwner);
            verificationCodeOwnerCookie.setPath("/");
            verificationCodeOwnerCookie.setMaxAge(120);
            response.addCookie(verificationCodeOwnerCookie);
        }

        //将验证码放入redis
        String verificationCodeKey = RedisKeyUtil.getVerificationCodeKey(verificationCodeOwner);
        redisTemplate.opsForValue().set(verificationCodeKey, randomCode);
        redisTemplate.expire(verificationCodeKey, 30, TimeUnit.SECONDS);

        BufferedImage bufferedImage = VerificationCodeUtil.getCodeImg(randomCode);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.setContentType("image/png");
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
