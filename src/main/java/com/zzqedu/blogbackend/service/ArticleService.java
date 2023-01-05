package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.vo.ArticleVo;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.ArticleParam;
import com.zzqedu.blogbackend.vo.param.PageParams;

import java.util.List;

public interface ArticleService {
    List<ArticleVo> listArticlePage(PageParams pageParams);

    Result hotArticles(int limit);

    Result newArticles(int limit);

    Result listArchives();


    Result getArticleById(String id);

    Result publishArticle(ArticleParam articleParam);


    Result findArticleById(Long articleId);

}
