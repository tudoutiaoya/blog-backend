package com.zzqedu.blogbackend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试hello接口")
@RestController
public class HelloController {

    @ApiOperation(value = "测试", notes = "测试接口")
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
