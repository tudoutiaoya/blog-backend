package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.dao.pojo.Category;

public interface CategoryService {
    Category findCategoryById(Integer categoryId);

}
