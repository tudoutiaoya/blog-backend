package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.service.CategoryService;
import com.zzqedu.blogbackend.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "文章分类")
@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @ApiOperation(value = "获取所有分类-id+name")
    @GetMapping
    public Result listCategory() {
        return categoryService.findAll();
    }

    @ApiOperation(value = "获取所有分类详细信息")
    @GetMapping("detail")
    public Result categoryDetails() {
        return categoryService.categoryDetails();
    }

    @ApiOperation(value = "查询某个分类信息(按照id查)")
    @GetMapping("/detail/{id}")
    public Result categoryDetailById(@PathVariable("id")String id) {
        return categoryService.categoryDetailById(id);
    }


}
