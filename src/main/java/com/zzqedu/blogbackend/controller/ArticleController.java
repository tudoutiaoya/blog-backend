package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.ArticleService;
import com.zzqedu.blogbackend.vo.ArticleVo;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.PageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Api(tags = "文章相关")
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Resource
    ArticleService articleService;

    @ApiOperation(value = "查询文章列表", notes = "查询文章列表  查文章、作者信息、tags信息")
    @PostMapping()
    public Result getArticlesList(@RequestBody PageParams pageParams) {
        List<ArticleVo> articles = articleService.listArticlePage(pageParams);
        return Result.success(articles);
    }

    @ApiOperation(value = "最热文章", notes = "观看数量最多的")
    @PostMapping("/hot")
    public Result hotArticles() {
        int limit = 5;
        return articleService.hotArticles(limit);
    }

    @ApiOperation(value = "最新文章", notes = "按照时间排序")
    @PostMapping("/new")
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
    public Result getArticleById(@PathVariable String id) {
        return articleService.getArticleById(id);
    }



}
