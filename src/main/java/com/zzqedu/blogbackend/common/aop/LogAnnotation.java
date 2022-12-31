package com.zzqedu.blogbackend.common.aop;


import java.lang.annotation.*;

// METHOD 注解定义在方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";
    String operation() default "";
}
