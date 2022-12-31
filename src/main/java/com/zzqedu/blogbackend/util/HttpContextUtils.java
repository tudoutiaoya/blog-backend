package com.zzqedu.blogbackend.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取request 请求工具类
 * RequestContextHolder 是Spirng提供的一个用来暴漏Request对象的工具，利用RequestContextHolder，可以在
 * 一个请求线程中获取到Request，避免了Request从投传到尾的情况
 */
public class HttpContextUtils {

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
