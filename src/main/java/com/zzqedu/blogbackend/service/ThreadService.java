package com.zzqedu.blogbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzqedu.blogbackend.dao.mapper.ArticleMapper;
import com.zzqedu.blogbackend.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThreadService {

    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts() + 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 这里很有艺术，为了在多线程的环境下 线程安全
        queryWrapper.eq(Article::getId, article.getId())
                .eq(Article::getViewCounts, article.getViewCounts());
        articleMapper.update(articleUpdate,queryWrapper);
        // 睡眠5s钟，证明不会影响主线程使用
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
