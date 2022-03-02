package com.bin.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    //生成UUID
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //Md5加密
    public static String Md5(String key) {
        if (StringUtils.isBlank(key))
            return null;
        else
            return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(String code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    public static String getJSONString(String code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(String code) {
        return getJSONString(code, null, null);
    }
}
