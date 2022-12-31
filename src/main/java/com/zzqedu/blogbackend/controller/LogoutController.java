package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.LoginService;
import com.zzqedu.blogbackend.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "登录注册")
@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Resource
    LoginService loginService;

    @ApiOperation(value = "删除token", notes = "redis中删除token")
    @GetMapping
    public Result logout(@RequestHeader("Authorization")String token) {
        return loginService.logout(token);
    }

}
