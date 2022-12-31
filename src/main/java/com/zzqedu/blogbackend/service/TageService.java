package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.TagVo;

import java.util.List;

public interface TageService {

    List<TagVo> findTagsByArticleId(Long id);

    List<TagVo> hot(int limit);

    Result findAll();
}
