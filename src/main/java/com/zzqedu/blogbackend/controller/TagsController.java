package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.TageService;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.TagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "文件标签")
@RestController
@RequestMapping("/tags")
public class TagsController {

    @Resource
    TageService tageService;

    @ApiOperation(value = "最热文章标签", notes = "标签对应的文章数量多")
    @GetMapping("/hot")
    public Result hot() {
        int limit = 6;
        List<TagVo> tagVos = tageService.hot(limit);
        return Result.success(tagVos);
    }

}
