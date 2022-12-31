package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.SysUserService;
import com.zzqedu.blogbackend.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "用户")
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    SysUserService sysUserService;

    @ApiOperation(value = "获取当前用户", notes = "根据token获取当前用户")
    @GetMapping("/currentUser")
    public Result getUserInfoByToken(@RequestHeader("Authorization")String token) {
        return sysUserService.getUserInfoByToken(token);
    }

}
