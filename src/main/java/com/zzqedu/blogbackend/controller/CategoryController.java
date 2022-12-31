package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.CategoryService;
import com.zzqedu.blogbackend.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "文章分类")
@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @ApiOperation(value = "获取所有分类")
    @GetMapping
    public Result listCategory() {
        return categoryService.findAll();
    }

}
