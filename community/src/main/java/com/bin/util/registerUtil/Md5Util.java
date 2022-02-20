package com.bin.util.registerUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.security.DigestException;
import java.util.UUID;

public class Md5Util {
    //生成UUID
    public static String generateUUID(){
       return UUID.randomUUID().toString().replace("-","");
    }
    //Md5加密
    public static String Md5(String key){
     if(StringUtils.isBlank(key))
         return null;
     else
         return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
