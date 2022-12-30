package com.zzqedu.blogbackend.handler;

import com.zzqedu.blogbackend.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AllExceptionHandler {

    // 统一异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception e) {
        e.printStackTrace();
        return Result.fail(-999,"系统异常");
    }

}
