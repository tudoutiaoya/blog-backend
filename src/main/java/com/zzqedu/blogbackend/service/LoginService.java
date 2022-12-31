package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.dao.pojo.SysUser;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.LoginParam;

public interface LoginService {
    Result login(LoginParam loginParam);

    Result logout(String token);

    Result register(LoginParam loginParam);

    SysUser checkToken(String token);

}
