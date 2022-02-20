package com.bin.service.impl;

import com.bin.bean.User;
import com.bin.dao.UserMapper;
import com.bin.util.registerUtil.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserMapper {
    @Autowired
    private UserMapper userMapper;

    //根据id查询用户所有信息
    @Override
    public User selectUserById(Integer id) {
        return userMapper.selectUserById(id);
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
        return userMapper.updateStatus(id, status);
    }

    //根据用户id更新用户的url
    @Override
    public int updateHeaderUrl(Integer id, String headerUrl) {
        return userMapper.updateHeaderUrl(id, headerUrl);
    }

    //根据id更新用户的密码
    @Override
    public int updatePassword(Integer id, String password) {
        return userMapper.updatePassword(id, password);
    }

    //判断用户注册信息是否符合要求
    public Map<String, String> judgeUserRegisterInfo(User user) {
        Map<String, String> mapInfo = new HashMap<>();
        if (user == null)
            throw new IllegalArgumentException("User参数不能为空！");
        else {
            //判断账号为空
            if (StringUtils.isBlank(user.getUsername())) {
                mapInfo.put("Info", "账号不能为空！");
                return mapInfo;
            }
            //判断密码为空
            if (StringUtils.isBlank(user.getPassword())) {
                mapInfo.put("Info", "密码不能为空！");
                return mapInfo;
            }
            //判断邮箱为空
            if (StringUtils.isBlank(user.getEmail())) {
                mapInfo.put("Info", "邮箱不能为空！");
                return mapInfo;
            }
            //判断账号已存在
            String username = user.getUsername();
            User userExistedForUsername = userMapper.selectUserByName(username);
            if (userExistedForUsername != null) {
                mapInfo.put("Info", "账号已存在！");
                return mapInfo;
            }
            //判断邮箱已注册
            String email = user.getEmail();
            User userExistedForMail = userMapper.selectUserByEmail(email);
            if (userExistedForMail != null) {
                mapInfo.put("Info", "邮箱已经被注册！");
                return mapInfo;
            }
            //判断两次密码不相同

            //注册用户
            String password = user.getPassword();
            String salt = Md5Util.generateUUID().substring(0, 5);
            String secretPassword = Md5Util.Md5(password + salt);
            user.setPassword(secretPassword);
            user.setCreateTime(new Date());
            user.set
        }
        return mapInfo;
    }
}
