package com.zzqedu.blogbackend.service.impl;

import com.zzqedu.blogbackend.dao.pojo.SysUser;
import com.zzqedu.blogbackend.mapper.SysUserMapper;
import com.zzqedu.blogbackend.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {


    @Resource
    SysUserMapper sysUserMapper;

    @Override
    public SysUser findSysUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("土豆条");
        }
        return sysUser;
    }
}
