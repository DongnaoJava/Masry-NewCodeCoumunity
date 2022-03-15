package com.bin.controller.advice;

import com.bin.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//spring默认会扫描所有的bean，所以要加上注解，限制扫描范围
@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler({Exception.class})
    public void handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("服务器发生异常: " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        //从请求头获取请求方式
        String xRequestWith = request.getHeader("x-requested-with");
        //表示是异步请求
        if ("XMLHttpRequest".equals(xRequestWith)) {
            //表示像浏览器返回的是一个普通的字符串，可以是字符串，但浏览器需要自己解析，就是那个date.parase()方法
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString("500", "服务器发生异常!"));
        } else
            response.sendRedirect(request.getContextPath()+"/error");
    }
}
