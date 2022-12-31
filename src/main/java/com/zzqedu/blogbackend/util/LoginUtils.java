package com.zzqedu.blogbackend.util;

import com.alibaba.fastjson2.JSON;
import com.zzqedu.blogbackend.dao.pojo.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Map;

public class LoginUtils {


    private static final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

    public static SysUser checkLoginToken(String token) {
        if(StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        if(map == null) {
            return null;
        }
        String userJson = stringRedisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(userJson)) {
            return null;
        }
        return JSON.parseObject(userJson, SysUser.class);
    }

}
