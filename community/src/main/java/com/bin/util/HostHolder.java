package com.bin.util;

import com.bin.bean.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    /**
     起到一个储存对象的功能，具有线程隔离的作用
     其实就是一个当前线程的map对象
     */
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
       return users.get();
    }

    public void removeUser(){
        users.remove();
    }
}
