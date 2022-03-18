package com.bin.controller.Interceptor;

import com.bin.bean.LoginTicket;
import com.bin.bean.User;
import com.bin.service.NoticeService;
import com.bin.service.impl.MessageServiceImpl;
import com.bin.service.impl.NoticeServiceImpl;
import com.bin.service.impl.TicketServiceImpl;
import com.bin.service.impl.UserServiceImpl;
import com.bin.util.CookieUtil;
import com.bin.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    /* @Autowired
     private TicketServiceImpl ticketService;*/
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private MessageServiceImpl messageService;
    @Autowired
    private NoticeServiceImpl noticeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String ticket = CookieUtil.getValueByKeyFromCookie("ticket", cookies);
        if (!StringUtils.isBlank(ticket)) {
            //已经登陆过了
            LoginTicket loginTicket = userServiceImpl.selectByTicket(ticket);
            if (loginTicket != null && loginTicket.getExpired().after(new Date()) && loginTicket.getStatus() == 0) {
                //登陆过了，且登录信息没有过期
                User user = userServiceImpl.selectUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (modelAndView != null && user != null) {
            modelAndView.addObject("loginUser", user);
            //所有的未读消息的数量
            Integer unreadAllLetterCount = messageService.selectAllMessagesUnreadRows(user.getId());
            modelAndView.addObject("unreadAllLetterCount", unreadAllLetterCount);
            Integer unreadAllNoticeCount = noticeService.selectUnreadNoticesRows(user.getId());
            modelAndView.addObject("unreadAllNoticeCount", unreadAllNoticeCount);
            modelAndView.addObject("unreadAllMessageCount", unreadAllLetterCount + unreadAllNoticeCount);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.removeUser();
    }
}
