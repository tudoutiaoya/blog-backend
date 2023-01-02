package com.zzqedu.blogbackend.common.cache;

import com.alibaba.fastjson2.JSON;
import com.zzqedu.blogbackend.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
@Slf4j
public class CacheAspect {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Pointcut("@annotation(com.zzqedu.blogbackend.common.cache.Cache)")
    public void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            Signature signature = joinPoint.getSignature();
            // 类名
            String className = joinPoint.getTarget().getClass().getName();
            // 调用的方法名
            String methodName = signature.getName();
            Class[] parameterTypes = new Class[joinPoint.getArgs().length];
            Object[] args = joinPoint.getArgs();
            log.info("函数的参数为：{}", JSON.toJSONString(args));
            // 参数
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if(args[i] != null) {
                    log.info("单个的函数参数为 {}", args[i]);
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if(StringUtils.isNotBlank(params)) {
                // 加密 防止出现key 过长以及字符串转义获取不到的情况
                // params = DigestUtils.md5DigestAsHex(params.getBytes());
            }
            Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            // 获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            // 缓存过期时间
            long expire = annotation.expire();
           // 缓存名称
            String name = annotation.name();
            // 先从redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            String redisValue = stringRedisTemplate.opsForValue().get(redisKey);
            if(StringUtils.isNotEmpty(redisValue)) {
                log.info("走了缓存~~~, {}", redisKey);
                Result result = JSON.parseObject(redisValue, Result.class);
                return result;
            }
            Object proceed = joinPoint.proceed();
            stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存~~~ {}, {}", className, methodName);
            return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return Result.fail(-999, "系统错误");
    }
}
