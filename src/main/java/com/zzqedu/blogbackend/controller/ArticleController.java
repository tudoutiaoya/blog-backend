package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.ArticleService;
import com.zzqedu.blogbackend.vo.ArticleVo;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.PageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

}
