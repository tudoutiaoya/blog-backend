package com.zzqedu.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzqedu.blogbackend.dao.pojo.SysUser;
import com.zzqedu.blogbackend.dao.mapper.SysUserMapper;
import com.zzqedu.blogbackend.service.LoginService;
import com.zzqedu.blogbackend.service.SysUserService;
import com.zzqedu.blogbackend.vo.ErrorCode;
import com.zzqedu.blogbackend.vo.LoginUserVo;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.UserVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {


    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    LoginService loginService;

    @Override
    public SysUser findSysUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("土豆条");
        }
        return sysUser;
    }

    @Override
    public SysUser findUse(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account)
                .eq(SysUser::getPassword,password)
                .select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname)
                .last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result getUserInfoByToken(String token) {
        SysUser sysUser = loginService.checkToken(token);
        if(sysUser == null) {
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId().toString());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account)
                .last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo getUserVoById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if(sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("土豆条");
        }
        UserVo userVo = new UserVo();
        userVo.setNickname(sysUser.getNickname());
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setId(sysUser.getId().toString());
        return userVo;
    }
}
