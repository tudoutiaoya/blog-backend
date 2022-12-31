package com.zzqedu.blogbackend.config;

import com.zzqedu.blogbackend.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    // 跨域设置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器 拦截接口
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/testInterceptor");
    }

}
