package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.dao.pojo.Category;
import com.zzqedu.blogbackend.vo.Result;

public interface CategoryService {
    Category findCategoryById(Long categoryId);

    Result findAll();


    Result categoryDetails();


    Result categoryDetailById(String id);
}
