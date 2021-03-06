package com.bin.service.impl;

import com.bin.bean.ActivationConsequence;
import com.bin.bean.LoginTicket;
import com.bin.bean.CommunityConstant;
import com.bin.bean.User;
import com.bin.dao.UserMapper;
import com.bin.util.MailSendUtil;
import com.bin.util.CommunityUtil;
import com.bin.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserMapper, CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailSendUtil mailSendUtil;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    /*@Autowired
    private TicketServiceImpl ticketService;*/
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //根据id查询用户所有信息
    @Override
    public User selectUserById(Integer id) {

        //return userMapper.selectUserById(id);
        User user = getUserByIdFromRedis(id);
        if (user == null)
            user = getUserByIdFromMySqlAndInitRedis(id);
        return user;
    }

    //根据用户名查询用户所有信息
    @Override
    public User selectUserByName(String username) {
        return userMapper.selectUserByName(username);
    }

    //根据用户邮箱查询用户所有信息
    @Override
    public User selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    //插入一条用户数据
    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    //根据用户id更新用户状态
    @Override
    public int updateStatus(Integer id, Integer status) {
        //redis和mysql的事务是分开的，所以先更新mysql，当mysql的数据更新成功，再删除缓存；
        int updateRows = userMapper.updateStatus(id, status);
        if (updateRows > 0)
            deleteUserFromRedis(id);
        return updateRows;
    }

    //根据用户id更新用户的url
    @Override
    public int updateHeaderUrl(Integer id, String headerUrl) {
        int updateRows = userMapper.updateHeaderUrl(id, headerUrl);
        if (updateRows > 0)
            deleteUserFromRedis(id);
        return updateRows;
    }

    //根据id更新用户的密码
    @Override
    public int updatePassword(Integer id, String password) {
        int updateRows = userMapper.updatePassword(id, password);
        if (updateRows > 0)
            deleteUserFromRedis(id);
        return updateRows;
    }

    //判断用户注册信息是否符合要求
    public Map<String, String> judgeUserRegisterInfo(User user) {
        Map<String, String> mapInfo = new HashMap<>();
        if (user == null)
            throw new IllegalArgumentException("User参数不能为空！");
        else {
            //判断账号为空
            if (StringUtils.isBlank(user.getUsername())) {
                mapInfo.put("accountInfo", "账号不能为空！");
                return mapInfo;
            }
            //判断密码为空
            if (StringUtils.isBlank(user.getPassword())) {
                mapInfo.put("passwordInfo", "密码不能为空！");
                return mapInfo;
            }
            //判断邮箱为空
            if (StringUtils.isBlank(user.getEmail())) {
                mapInfo.put("emailInfo", "邮箱不能为空！");
                return mapInfo;
            }
            //判断账号已存在
            String username = user.getUsername();
            User userExistedForUsername = userMapper.selectUserByName(username);
            if (userExistedForUsername != null) {
                mapInfo.put("accountInfo", "账号已存在！");
                return mapInfo;
            }
            //判断邮箱已注册
            String email = user.getEmail();
            User userExistedForMail = userMapper.selectUserByEmail(email);
            if (userExistedForMail != null) {
                mapInfo.put("emailInfo", "邮箱已经被注册！");
                return mapInfo;
            }
            //判断两次密码不相同
            {
                // 已经在前端页面中实现控制
            }
            //注册用户
            user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
            user.setPassword(CommunityUtil.Md5(user.getPassword() + user.getSalt()));
            user.setType(0);
            user.setStatus(0);
            user.setActivationCode(CommunityUtil.generateUUID());
            user.setHeaderUrl("http://api.btstu.cn/sjtx/api.php?lx=c1&format=images");
            user.setCreateTime(new Date());
            userMapper.insertUser(user);

            //向这个html模板文件中传入参数，需要借助这个context，模板引擎会自动根据传入的参数整合到最终的html文件中
            Context context = new Context();
            context.setVariable("SendTo", user.getUsername());
            String url = domain + "/activation/" + user.getId() + "/" + user.getActivationCode();
            context.setVariable("url", url);
            String content = templateEngine.process("/mail/activation.html", context);
            mailSendUtil.Sendmail(user.getEmail(), "Activate your account!", content);
        }
        return mapInfo;
    }

    //激活用户
    public Enum<ActivationConsequence> activation(Integer id, String activationCode) {
        User user = userMapper.selectUserById(id);
        if (user != null && user.getActivationCode() != null && user.getActivationCode().equals(activationCode)) {
            if (user.getStatus() == 0) {
                userMapper.updateStatus(id, 1);
                return ActivationConsequence.ACTIVATION_SUCCESS;
            } else
                return ActivationConsequence.ACTIVATION_REPEAT;
        } else {
            return ActivationConsequence.ACTIVATION_FAILURE;
        }
    }

    //验证用户登录信息
    public Map<String, String> judgeUserLoginInfo(String username, String password, Integer expiredTime) {
        Map<String, String> mapInfo = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            mapInfo.put("accountInfo", "账号不能为空！");
            return mapInfo;
        }
        if (StringUtils.isBlank(password)) {
            mapInfo.put("passwordInfo", "密码不能为空！");
            return mapInfo;
        }
        /* if (StringUtils.isBlank(verificationCode)) {
            mapInfo.put("verificationCodeInfo", "验证码不能为空!");
            return mapInfo;
        }*/
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            mapInfo.put("accountInfo", "账号不存在！");
            return mapInfo;
        } else {
            if (user.getStatus() == 0) {
                mapInfo.put("accountInfo", "该账号未激活！");
                return mapInfo;
            }
            //判读密码正不正确
            String userInputPassword = CommunityUtil.Md5(password + user.getSalt());
            String userPassword = user.getPassword();
            if (!userInputPassword.equals(userPassword)) {
                mapInfo.put("passwordInfo", "密码不正确！");
                return mapInfo;
            }
        }

        //生成登陆凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        //账号有效状态为0，无效为1，与账号是否激活的status不是一个概念
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredTime * 1000));
        //ticketService.insertTicket(loginTicket);

        //loginTicket保存在redis中
        String ticketKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(ticketKey, loginTicket);
        redisTemplate.expire(ticketKey, 60, TimeUnit.MINUTES);
        mapInfo.put("ticket", loginTicket.getTicket());
        return mapInfo;
    }

    //用户第一次登录
    public String firstLogin(String username, String password, Model model, String verificationCodeOwner,
                             String verificationCode, HttpServletResponse response, boolean remember) {
        String target;
        int expiredTime = remember ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, String> mapInfo = judgeUserLoginInfo(username, password, expiredTime);
        if (mapInfo.get("ticket") != null) {
            String verificationCodeKey = RedisKeyUtil.getVerificationCodeKey(verificationCodeOwner);
            String correctCode = (String) redisTemplate.opsForValue().get(verificationCodeKey);
            if (verificationCode == null) {
                model.addAttribute("codeInfo", "验证码不能为空！");
                target = "login";
            } else {
                if (correctCode == null) {
                    model.addAttribute("codeInfo", "验证码已过期！");
                    target = "login";
                } else {
                    if (!verificationCode.equalsIgnoreCase(correctCode)) {
                        model.addAttribute("codeInfo", "验证码错误！");
                        target = "login";
                    } else {
                        Cookie cookie = new Cookie("ticket", mapInfo.get("ticket"));
                        cookie.setPath("/");
                        cookie.setMaxAge(expiredTime);
                        response.addCookie(cookie);
                        target = "index";
                    }
                }
            }
        } else {
            model.addAttribute("accountInfo", mapInfo.get("accountInfo"));
            model.addAttribute("passwordInfo", mapInfo.get("passwordInfo"));
            target = "login";
        }
        return target;
    }

    //用户退出登录
    public void logout(String ticket) {
        //ticketService.updateStatusByTicket(ticket, 1);
        //将loginTicket的状态改为1
        updateLoginTicketStatus(ticket, 1);
    }

    //根据ticket查询loginTicket
    public LoginTicket selectByTicket(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
    }

    //更新longinTicket的状态
    public void updateLoginTicketStatus(String ticket, Integer status) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        if (loginTicket == null)
            throw new IllegalArgumentException("redis数据库发生错误！");
        loginTicket.setStatus(status);
        redisTemplate.opsForValue().set(ticketKey, loginTicket);
    }

    //优先从缓存中取用户信息
    private User getUserByIdFromRedis(Integer userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(userKey);
    }

    //娶不到初始化缓存
    private User getUserByIdFromMySqlAndInitRedis(Integer userId) {
        User user = userMapper.selectUserById(userId);
        String userKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(userKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    //数据变化时删除缓存
    private void deleteUserFromRedis(Integer userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(userKey);
    }
}









