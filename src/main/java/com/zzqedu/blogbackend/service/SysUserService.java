package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.dao.pojo.SysUser;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.UserVo;
import org.springframework.stereotype.Service;

@Service
public interface SysUserService {
    SysUser findSysUserById(Long id);

    SysUser findUse(String account, String password);

    Result getUserInfoByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo getUserVoById(Long authorId);

}
