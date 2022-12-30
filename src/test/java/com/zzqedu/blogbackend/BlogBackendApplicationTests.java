package com.zzqedu.blogbackend;

import com.zzqedu.blogbackend.dao.mapper.ArticleMapper;
import com.zzqedu.blogbackend.dao.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

@SpringBootTest
class BlogBackendApplicationTests {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        ValueOperations<String, String> value = stringRedisTemplate.opsForValue();
        value.set("name","zzq");
        String name = value.get("name");
        System.out.println(name);
    }

    @Resource
    ArticleMapper articleMapper;

    @Test
    void testMP() {
        Article article = new Article();
        article.setCommentCounts(0);
        article.setCreateDate(0L);
        article.setSummary("概述");
        article.setTitle("标题");
        article.setViewCounts(0);
        article.setWeight(0);
        article.setAuthorId(0L);
        article.setBodyId(0L);
        article.setCategoryId(0);
        articleMapper.insert(article);
    }

    @Test
    void testArray() {
        ArrayList<Integer> integers = new ArrayList<>();
        String[] array = new String[]{};
        Arrays.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 0;
            }
        });
    }



}
