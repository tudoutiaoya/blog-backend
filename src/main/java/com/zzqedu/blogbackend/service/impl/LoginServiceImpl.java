package com.zzqedu.blogbackend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.zzqedu.blogbackend.dao.pojo.SysUser;
import com.zzqedu.blogbackend.service.LoginService;
import com.zzqedu.blogbackend.service.SysUserService;
import com.zzqedu.blogbackend.util.JWTUtils;
import com.zzqedu.blogbackend.vo.ErrorCode;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.LoginParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private static final String slat = "mszlu!@#";

    @Autowired
    SysUserService sysUserService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去表中查
         * 3. 如果不存在，登录失败
         * 4. 如果存在，使用jwt，生成token，返回给前端
         * 5. token放入redis当中，redis token: user信息 设置过期时间
         * （登录认证的时候，先认证token字符串是否合法，去redis认证是否存在）
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5DigestAsHex((password+slat).getBytes());
        SysUser sysUser = sysUserService.findUse(account,password);
        if(sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        // 登录成功
        String token = JWTUtils.createToken(sysUser.getId());
        // 设置一天过期时间
        stringRedisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        stringRedisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String nickname = loginParam.getNickname();
        String password = loginParam.getPassword();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(nickname)
        || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if(sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();

        sysUser.setAccount(account);
        sysUser.setAdmin(false);
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setDeleted(false);
        sysUser.setEmail("");
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setMobilePhoneNumber("");
        sysUser.setNickname(nickname);
        sysUser.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        sysUser.setSalt("");
        sysUser.setStatus("");

        sysUserService.save(sysUser);
        String token = JWTUtils.createToken(sysUser.getId());
        stringRedisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
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
