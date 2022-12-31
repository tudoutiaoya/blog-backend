package com.zzqedu.blogbackend.service.impl;

import com.zzqedu.blogbackend.dao.mapper.CategoryMapper;
import com.zzqedu.blogbackend.dao.pojo.Category;
import com.zzqedu.blogbackend.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public Category findCategoryById(Integer categoryId) {
        return categoryMapper.selectById(categoryId);
    }
}
