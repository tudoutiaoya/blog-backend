package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.CommentService;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.CommentParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "评论")
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Resource
    CommentService commentService;

    @ApiOperation(value = "查询文章评论列表", notes = "查出一级(level 为 1)评论信息 作者信息 查询二级评论信息 作者信息 + 给谁评论")
    @GetMapping("article/{id}")
    public Result getArticleComments(@PathVariable("id")String id) {
        return commentService.getArticleComments(id);
    }

    @ApiOperation(value = "评论-加入到登录拦截器", notes = "评论 用户id从threadlocal获取")
    @PostMapping("create/change")
    public Result createComment(@RequestBody CommentParam commentParam) {
        return commentService.createComment(commentParam);
    }

}
