package com.zzqedu.blogbackend.service.mq;

import com.zzqedu.blogbackend.config.RabbitConfig;
import com.zzqedu.blogbackend.dao.pojo.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
@Slf4j
public class ArticleUpdateListener {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queues = RabbitConfig.UPDATE_QUEUE)
    public void handler(SysUser user) {
        log.info("用户{}，修改了文章，更新缓存", user.getNickname());
        // 删除缓存  文章列表
        Set<String> keys = stringRedisTemplate.keys("listArticle*");
        String key = keys.iterator().next();
        if(key != null) {
            stringRedisTemplate.delete(key);
            log.info("删除了文章缓存: {}", key);
        }
    }

}
