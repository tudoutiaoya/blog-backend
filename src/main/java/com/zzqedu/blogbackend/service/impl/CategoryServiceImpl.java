package com.zzqedu.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzqedu.blogbackend.dao.mapper.CategoryMapper;
import com.zzqedu.blogbackend.dao.pojo.Category;
import com.zzqedu.blogbackend.service.CategoryService;
import com.zzqedu.blogbackend.vo.CategoryVo;
import com.zzqedu.blogbackend.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(copyList(categories));
    }

    @Override
    public Result categoryDetails() {
        List<Category> categories = categoryMapper.selectList(null);
        return Result.success(copyList(categories));
    }

    @Override
    public Result categoryDetailById(String id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    private List<CategoryVo> copyList(List<Category> categories) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categories) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(category.getId().toString());
        return categoryVo;
    }
}
