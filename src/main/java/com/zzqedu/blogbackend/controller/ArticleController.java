package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.common.aop.LogAnnotation;
import com.zzqedu.blogbackend.common.cache.Cache;
import com.zzqedu.blogbackend.service.ArticleService;
import com.zzqedu.blogbackend.vo.ArticleVo;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.ArticleParam;
import com.zzqedu.blogbackend.vo.param.PageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "文章相关")
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Resource
    ArticleService articleService;

    @LogAnnotation(module="文章", operation="获取文章列表")
    @ApiOperation(value = "查询文章列表", notes = "查询文章列表  查文章、作者信息、tags信息")
    @PostMapping()
    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    public Result getArticlesList(@RequestBody PageParams pageParams) {
        List<ArticleVo> articles = articleService.listArticlePage(pageParams);
        return Result.success(articles);
    }

    @ApiOperation(value = "最热文章", notes = "观看数量最多的")
    @PostMapping("/hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticles() {
        int limit = 5;
        return articleService.hotArticles(limit);
    }

    @ApiOperation(value = "最新文章", notes = "按照时间排序")
    @PostMapping("/new")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result newArticles() {
        int limit = 5;
        return articleService.newArticles(limit);
    }


    @ApiOperation(value = "文章归档", notes = "查询 年份-月份 数量")
    @PostMapping("/listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    @ApiOperation(value = "查看文章详情", notes = "v1: 查找文章body category tags author ，使用线程池更新文章阅读数量，不会影响主线程执行")
    @PostMapping("/view/{id}")
    // @Cache(expire = 5 * 60 * 1000,name = "view_article")
    public Result getArticleById(@PathVariable String id) {
        return articleService.getArticleById(id);
    }

    @ApiOperation(value = "发表文章", notes = "插入文章 插入内容表 插入文章-标签关系表")
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publishArticle(articleParam);
    }



}
