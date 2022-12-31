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
@RequestMapping("/register")
public class RegisterController {

    @Resource
    LoginService loginService;

    @ApiOperation(value = "注册", notes = "验证是否存在 - 注册 - redis存token - 返回token loginservice中加上事务")
    @PostMapping
    public Result register(@RequestBody LoginParam loginParam) {
        return loginService.register(loginParam);
    }

}
