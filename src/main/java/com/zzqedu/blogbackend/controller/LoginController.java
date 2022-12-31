package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.LoginService;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.LoginParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "登录注册")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    LoginService loginService;

    @ApiOperation(value = "登录", notes = "登录 - 生成token - 存入redis - 返回token")
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }

}
