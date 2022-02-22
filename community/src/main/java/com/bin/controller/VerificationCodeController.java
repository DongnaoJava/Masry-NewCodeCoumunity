package com.bin.controller;

import com.bin.util.loginUtil.VerificationCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class VerificationCodeController {
    @ResponseBody
    @GetMapping("/code")
    public void getCode(HttpServletResponse response, HttpSession session) {
        String randomCode = VerificationCodeUtil.getRandomCode(4);
        session.setAttribute("verificationCode",randomCode);
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
