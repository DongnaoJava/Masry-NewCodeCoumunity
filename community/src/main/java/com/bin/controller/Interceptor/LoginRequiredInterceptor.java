package com.bin.controller.Interceptor;

import com.bin.annotation.LoginRequired;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.getAnnotation(LoginRequired.class) != null && hostHolder.getUser() == null) {
                //从请求头获取请求方式
                String xRequestWith = request.getHeader("x-requested-with");
                if ("XMLHttpRequest".equals(xRequestWith)) {
                    //表示像浏览器返回的是一个普通的字符串，可以是字符串，但浏览器需要自己解析，就是那个date.parase()方法
                    response.setContentType("application/plain;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(CommunityUtil.getJSONString("401", "您还没有登陆!"));
                } else
                    response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
