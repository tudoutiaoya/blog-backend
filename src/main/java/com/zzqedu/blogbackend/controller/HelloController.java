package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试")
@RestController
public class HelloController {

    @ApiOperation(value = "测试", notes = "测试接口")
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @ApiOperation(value = "测试拦截器", notes = "测试接口")
    @GetMapping("/testInterceptor")
    public Result test() {
        return Result.success("test");
    }

}
